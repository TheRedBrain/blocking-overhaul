package com.github.theredbrain.blockingoverhaul.config;

import com.github.theredbrain.blockingoverhaul.BlockingOverhaul;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;

public class ServerConfig extends Config {

    public ServerConfig() {
        super(BlockingOverhaul.identifier("server"));
    }

    public ValidatedBoolean enable_blocking_overhaul = new ValidatedBoolean(true);
    public ValidatedBoolean parrying_multiplies_blocked_damage = new ValidatedBoolean(true);
    public ValidatedBoolean parrying_multiplies_knockback = new ValidatedBoolean(true);
    public ValidatedFloat total_block_force_multiplier = new ValidatedFloat(1.0F);

    public ValidatedFloat natural_block_force = new ValidatedFloat(0.0F);
    public ValidatedFloat natural_parry_multiplier = new ValidatedFloat(1.0F);
    public ValidatedFloat natural_parry_window = new ValidatedFloat(0.0F);
    public ValidatedFloat natural_action_stamina_cost_attack_parrying = new ValidatedFloat(1.0F);
}