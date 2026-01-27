package com.github.theredbrain.blockingoverhaul.compatibility;

import com.github.theredbrain.overhauleddamage.OverhauledDamage;

public class OverhauledDamageIntegration {

    public static boolean isBlockingOverhaulActive() {
        return OverhauledDamage.isBlockingOverhaulEnabled();
    }
}
