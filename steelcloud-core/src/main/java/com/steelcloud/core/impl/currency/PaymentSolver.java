package com.steelcloud.core.impl.currency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 有限硬币找零 DP。使用二进制拆分的多重背包，
 * 对非规范面额（如 1/5/20/50/100）也能保证有解时必找到。
 */
final class PaymentSolver {
    private PaymentSolver() {}

    /** 恰好凑出 target；返回每种面额使用张数，无解返回 null。 */
    static int[] solveExact(int target, int[] denominations, int[] stock) {
        if (target < 0) return null;
        if (target == 0) return new int[denominations.length];

        List<int[]> items = new ArrayList<>(); // {denomIndex, count, value}
        for (int i = 0; i < denominations.length; i++) {
            int s = stock[i];
            int k = 1;
            while (s > 0) {
                int take = Math.min(k, s);
                items.add(new int[]{i, take, denominations[i] * take});
                s -= take;
                k <<= 1;
            }
        }

        int[] dp = new int[target + 1];
        Arrays.fill(dp, -1);
        dp[0] = 0;
        int[][] from = new int[target + 1][]; // {itemIndex, prevAmount}

        for (int idx = 0; idx < items.size(); idx++) {
            int[] it = items.get(idx);
            int val = it[2];
            int cnt = it[1];
            for (int amt = target; amt >= val; amt--) {
                if (dp[amt - val] == -1) continue;
                int newCount = dp[amt - val] + cnt;
                if (dp[amt] == -1 || newCount < dp[amt]) {
                    dp[amt] = newCount;
                    from[amt] = new int[]{idx, amt - val};
                }
            }
        }

        if (dp[target] == -1) return null;

        int[] plan = new int[denominations.length];
        int cur = target;
        while (cur > 0) {
            int[] f = from[cur];
            int[] it = items.get(f[0]);
            plan[it[0]] += it[1];
            cur = f[1];
        }
        return plan;
    }

    /** 返回 [min, max] 区间内所有可达金额（升序）。 */
    static List<Integer> reachableAmounts(int min, int max, int[] denominations, int[] stock) {
        if (max < 0) return List.of();
        boolean[] reach = new boolean[max + 1];
        reach[0] = true;
        for (int i = 0; i < denominations.length; i++) {
            int s = stock[i];
            int k = 1;
            while (s > 0) {
                int take = Math.min(k, s);
                int val = denominations[i] * take;
                for (int amt = max; amt >= val; amt--) {
                    if (reach[amt - val]) reach[amt] = true;
                }
                s -= take;
                k <<= 1;
            }
        }
        List<Integer> result = new ArrayList<>();
        int lo = Math.max(0, min);
        for (int amt = lo; amt <= max; amt++) {
            if (reach[amt]) result.add(amt);
        }
        return result;
    }
}