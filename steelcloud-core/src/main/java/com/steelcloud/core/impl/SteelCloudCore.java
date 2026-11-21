package com.steelcloud.core.impl;

import com.steelcloud.core.api.SteelCloudServices;
import com.steelcloud.core.api.currency.CurrencyService;
import com.steelcloud.core.api.currency.PaymentService;
import com.steelcloud.core.impl.command.SteelCloudCommand;
import com.steelcloud.core.impl.content.materials.SteelCloudMaterials;
import com.steelcloud.core.impl.currency.CurrencyServiceImpl;
import com.steelcloud.core.impl.currency.PaymentServiceImpl;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SteelCloudCore implements ModInitializer {
    public static final String MOD_ID = "steelcloud-core";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // 注册核心自带的 currency 实现
        CurrencyServiceImpl currencyService = new CurrencyServiceImpl();
        SteelCloudServices.register(CurrencyService.class, currencyService);
        SteelCloudServices.register(PaymentService.class, new PaymentServiceImpl(currencyService));

        SteelCloudMaterials.register();
        SteelCloudCommand.register();
        LOGGER.info("SteelCloud Core initialized (api_version={})", "0.1.0");
    }
}