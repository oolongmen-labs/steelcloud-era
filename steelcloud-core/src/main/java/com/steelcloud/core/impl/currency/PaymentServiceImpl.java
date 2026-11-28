package com.steelcloud.core.impl.currency;

import com.steelcloud.core.api.currency.CurrencyService;
import com.steelcloud.core.api.currency.CurrencySpec;
import com.steelcloud.core.api.currency.Denomination;
import com.steelcloud.core.api.currency.PaymentResult;
import com.steelcloud.core.api.currency.PaymentService;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class PaymentServiceImpl implements PaymentService {
    /** 单笔支付金额上限，后续可改为从配置文件读取。 */
    private static final long MAX_AMOUNT = 100_000L;

    private final CurrencyService currencyService;

    public PaymentServiceImpl(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public PaymentResult pay(ServerPlayerEntity payer, ServerPlayerEntity payee,
                             Identifier currencyId, long amount) {
        return doPay(payer.getInventory(), payer.getPos(), payer.getServerWorld(),
                payee.getInventory(), payee.getPos(), payee.getServerWorld(),
                currencyId, amount);
    }

    @Override
    public PaymentResult payToInventory(ServerPlayerEntity payer, PlayerInventory payeeInv,
                                        Vec3d payeeDropPos, Identifier currencyId, long amount) {
        ServerWorld payeeWorld = payer.getServerWorld();
        return doPay(payer.getInventory(), payer.getPos(), payer.getServerWorld(),
                payeeInv, payeeDropPos, payeeWorld, currencyId, amount);
    }

    @Override
    public PaymentResult payToVoid(ServerPlayerEntity payer, Identifier currencyId,
                                   long amount, String reason) {
        if (amount <= 0) return new PaymentResult.InvalidAmount(amount);
        if (amount > MAX_AMOUNT) return new PaymentResult.AmountTooLarge(amount, MAX_AMOUNT);

        CurrencySpec spec = currencyService.getCurrency(currencyId).orElse(null);
        if (spec == null) return new PaymentResult.CurrencyNotRegistered(currencyId);

        Wallet wallet = Wallet.scan(payer.getInventory(), spec);
        long total = wallet.totalValue();
        if (total < amount) return new PaymentResult.InsufficientFunds(amount, total);

        int[] plan = PaymentSolver.solveExact((int) amount, wallet.denomValues(), wallet.stocks());
        if (plan == null) {
            return new PaymentResult.CannotMakeChange(amount, total);
        }
        for (int i = 0; i < plan.length; i++) {
            if (plan[i] > 0) {
                removeFromInventory(payer.getInventory(), spec.denominations().get(i).item(), plan[i]);
            }
        }
        return new PaymentResult.Success(amount);
    }

    @Override
    public PaymentResult refund(ServerPlayerEntity payee, Identifier currencyId,
                                long amount, String reason) {
        if (amount <= 0) return new PaymentResult.InvalidAmount(amount);
        if (amount > MAX_AMOUNT) return new PaymentResult.AmountTooLarge(amount, MAX_AMOUNT);

        CurrencySpec spec = currencyService.getCurrency(currencyId).orElse(null);
        if (spec == null) return new PaymentResult.CurrencyNotRegistered(currencyId);

        ServerWorld world = payee.getServerWorld();
        // 用贪心生成退款方案（系统凭空生成，无找零限制）
        long remaining = amount;
        List<Denomination> denoms = spec.denominations();
        for (Denomination d : denoms) {
            if (remaining <= 0) break;
            long count = remaining / d.value();
            if (count <= 0) continue;
            insertOrDrop(payee.getInventory(), world, payee.getPos(), d.item(), (int) count);
            remaining -= count * d.value();
        }
        if (remaining > 0) {
            return new PaymentResult.CannotMakeChange(amount, amount - remaining);
        }
        return new PaymentResult.Success(amount);
    }

    @Override
    public boolean canPay(ServerPlayerEntity payer, Identifier currencyId, long amount) {
        if (amount <= 0) return false;
        if (amount > MAX_AMOUNT) return false;
        CurrencySpec spec = currencyService.getCurrency(currencyId).orElse(null);
        if (spec == null) return false;
        Wallet wallet = Wallet.scan(payer.getInventory(), spec);
        return wallet.totalValue() >= amount;
    }

    // ---- 内部实现 ----

    private PaymentResult doPay(Inventory payerInv, Vec3d payerDrop, ServerWorld payerWorld,
                                Inventory payeeInv, Vec3d payeeDrop, ServerWorld payeeWorld,
                                Identifier currencyId, long amount) {
        if (amount <= 0) return new PaymentResult.InvalidAmount(amount);
        if (amount > MAX_AMOUNT) return new PaymentResult.AmountTooLarge(amount, MAX_AMOUNT);

        CurrencySpec spec = currencyService.getCurrency(currencyId).orElse(null);
        if (spec == null) return new PaymentResult.CurrencyNotRegistered(currencyId);

        Wallet payerW = Wallet.scan(payerInv, spec);
        Wallet payeeW = Wallet.scan(payeeInv, spec);

        long payerTotal = payerW.totalValue();
        if (payerTotal < amount) {
            return new PaymentResult.InsufficientFunds(amount, payerTotal);
        }

        // 阶段 1：精确支付
        int[] payPlan = PaymentSolver.solveExact((int) amount, payerW.denomValues(), payerW.stocks());
        if (payPlan != null) {
            int[] changePlan = new int[spec.denominations().size()];
            execute(payerInv, payeeInv, spec, payPlan, changePlan,
                    payerWorld, payeeWorld, payerDrop, payeeDrop);
            return new PaymentResult.Success(amount);
        }

        // 阶段 2：超额找零
        long upper = Math.min(payerTotal, amount + payeeW.totalValue());
        if (upper > MAX_AMOUNT) upper = MAX_AMOUNT;
        if (upper <= amount) {
            return new PaymentResult.CannotMakeChange(amount, payerTotal);
        }
        List<Integer> candidates = PaymentSolver.reachableAmounts(
                (int) amount + 1, (int) upper, payerW.denomValues(), payerW.stocks());
        int[] payeeDenoms = payeeW.denomValues();
        int[] payeeStocks = payeeW.stocks();
        for (int v : candidates) {
            int change = v - (int) amount;
            int[] changePlan = PaymentSolver.solveExact(change, payeeDenoms, payeeStocks);
            if (changePlan != null) {
                int[] payPlan2 = PaymentSolver.solveExact(v, payerW.denomValues(), payerW.stocks());
                if (payPlan2 == null) continue;
                execute(payerInv, payeeInv, spec, payPlan2, changePlan,
                        payerWorld, payeeWorld, payerDrop, payeeDrop);
                return new PaymentResult.Success(amount);
            }
        }

        return new PaymentResult.CannotMakeChange(amount, payerTotal);
    }

    private void execute(Inventory payerInv, Inventory payeeInv, CurrencySpec spec,
                         int[] payPlan, int[] changePlan,
                         ServerWorld payerWorld, ServerWorld payeeWorld,
                         Vec3d payerDrop, Vec3d payeeDrop) {
        // 顺序：先扣买家，再扣卖家，再给卖家，再给买家
        for (int i = 0; i < spec.denominations().size(); i++) {
            Denomination d = spec.denominations().get(i);
            if (payPlan[i] > 0) removeFromInventory(payerInv, d.item(), payPlan[i]);
            if (changePlan[i] > 0) removeFromInventory(payeeInv, d.item(), changePlan[i]);
            if (payPlan[i] > 0) insertOrDrop(payeeInv, payeeWorld, payeeDrop, d.item(), payPlan[i]);
            if (changePlan[i] > 0) insertOrDrop(payerInv, payerWorld, payerDrop, d.item(), changePlan[i]);
        }
    }

    private static void removeFromInventory(Inventory inv, Item item, int count) {
        for (int slot = 0; slot < inv.size() && count > 0; slot++) {
            ItemStack stack = inv.getStack(slot);
            if (stack.isEmpty() || !stack.isOf(item)) continue;
            int take = Math.min(count, stack.getCount());
            stack.decrement(take);
            if (stack.isEmpty()) inv.setStack(slot, ItemStack.EMPTY);
            count -= take;
        }
    }

    private static void insertOrDrop(Inventory inv, ServerWorld world, Vec3d dropPos,
                                     Item item, int count) {
        int maxStack = item.getMaxCount();
        while (count > 0) {
            int chunk = Math.min(count, maxStack);
            ItemStack stack = new ItemStack(item, chunk);
            insertToInventory(inv, world, dropPos, stack);
            count -= chunk;
        }
    }

    private static void insertToInventory(Inventory inv, ServerWorld world,
                                          Vec3d dropPos, ItemStack stack) {
        // 先试合并到已有非满堆
        for (int slot = 0; slot < inv.size() && !stack.isEmpty(); slot++) {
            ItemStack existing = inv.getStack(slot);
            if (existing.isEmpty()) continue;
            if (!ItemStack.areEqual(existing, stack)) continue;
            int space = existing.getMaxCount() - existing.getCount();
            if (space <= 0) continue;
            int take = Math.min(space, stack.getCount());
            existing.increment(take);
            stack.decrement(take);
        }
        // 再试放入空槽
        for (int slot = 0; slot < inv.size() && !stack.isEmpty(); slot++) {
            if (inv.getStack(slot).isEmpty()) {
                inv.setStack(slot, stack.copy());
                stack.setCount(0);
            }
        }
        // 剩余掉落
        if (!stack.isEmpty()) {
            ItemEntity entity = new ItemEntity(world, dropPos.x, dropPos.y, dropPos.z, stack);
            entity.setPickupDelay(10);
            world.spawnEntity(entity);
        }
    }
}