package com.github.theredbrain.blockingoverhaul.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;

public record ParriesAttacksDataComponent(
        boolean multiplier_applies_to_knockback,
        Optional<Holder<SoundEvent>> parry_sound
) {
    public static final ParriesAttacksDataComponent DEFAULT = new ParriesAttacksDataComponent(true, Optional.empty());
    public static final Codec<ParriesAttacksDataComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.BOOL.optionalFieldOf("multiplier_applies_to_knockback", true).forGetter(ParriesAttacksDataComponent::multiplier_applies_to_knockback),
                            SoundEvent.CODEC.optionalFieldOf("parry_sound").forGetter(ParriesAttacksDataComponent::parry_sound)
                    )
                    .apply(instance, ParriesAttacksDataComponent::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ParriesAttacksDataComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ParriesAttacksDataComponent::multiplier_applies_to_knockback,
            SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional),
            ParriesAttacksDataComponent::parry_sound,
            ParriesAttacksDataComponent::new
    );

    public void onParried(ServerLevel serverLevel, LivingEntity livingEntity) {
        this.parry_sound
                .ifPresent(
                        holder -> serverLevel.playSound(
                                null,
                                livingEntity.getX(),
                                livingEntity.getY(),
                                livingEntity.getZ(),
                                holder,
                                livingEntity.getSoundSource(),
                                1.0F,
                                0.8F + serverLevel.random.nextFloat() * 0.4F
                        )
                );
    }

}
