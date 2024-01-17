package com.pancake.surviving_the_aftermath.util;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


import java.util.*;

public class CodecUtils {
    public static final Codec<UUID> UUID_CODEC = Codec.STRING
            .xmap(UUID::fromString, UUID::toString)
            .fieldOf("uuid")
            .codec();

    public static final Codec<AttributeModifier.Operation> ATTRIBUTE_MODIFIER_OPERATION_CODEC = Codec.INT
            .xmap(AttributeModifier.Operation::fromValue, AttributeModifier.Operation::toValue);
    public static final Codec<AttributeModifier> ATTRIBUTE_MODIFIER_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("uuid").forGetter(AttributeModifier::getName),
            Codec.DOUBLE.fieldOf("amount").forGetter(AttributeModifier::getAmount),
            ATTRIBUTE_MODIFIER_OPERATION_CODEC.fieldOf("operation").forGetter(AttributeModifier::getOperation)
    ).apply(instance, AttributeModifier::new));

    public static final Codec<MobEffectInstance> MOB_EFFECT_INSTANCE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(MobEffectInstance::getEffect),
            Codec.INT.fieldOf("duration").forGetter(MobEffectInstance::getDuration),
            Codec.INT.fieldOf("amplifier").forGetter(MobEffectInstance::getAmplifier)
    ).apply(instance, MobEffectInstance::new));



    //T
    public static <T> Codec<Set<T>> setOf(Codec<T> codec) {
        return Codec.list(codec).xmap(HashSet::new, ArrayList::new);
    }

    public static <K,V> Codec<Map<K,V>> mapOf(Codec<K> codec, Codec<V> codec2) {
        return Codec.unboundedMap(codec, codec2).xmap(HashMap::new, HashMap::new);
    }

}
