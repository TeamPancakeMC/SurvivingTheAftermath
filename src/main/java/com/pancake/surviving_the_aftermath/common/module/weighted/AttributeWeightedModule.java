package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.util.CodecUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public class AttributeWeightedModule extends BaseWeightedModule<AttributeWeightedModule.AttributeInfo>{
    public static final String IDENTIFIER = "attribute_weighted";

    public static final Codec<AttributeWeightedModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WeightedEntry.Wrapper.codec(AttributeInfo.CODEC).listOf().fieldOf("list").forGetter(AttributeWeightedModule::getList)
    ).apply(instance, AttributeWeightedModule::new));

    public AttributeWeightedModule(List<WeightedEntry.Wrapper<AttributeInfo>> list) {
        super(list);
    }

    public AttributeWeightedModule() {
    }


    @Override
    public Codec<? extends IWeightedModule<AttributeInfo>> codec() {
        return CODEC;
    }

    @Override
    public IWeightedModule<AttributeInfo> type() {
        return ModAftermathModule.ATTRIBUTE_WEIGHTED.get();
    }

    public record AttributeInfo(Attribute attribute, AttributeModifier attributeModifier) {
        public static Codec<AttributeInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.ATTRIBUTE.byNameCodec().fieldOf("attribute").forGetter(AttributeInfo::attribute),
                CodecUtils.ATTRIBUTE_MODIFIER_CODEC.fieldOf("attribute_modifier").forGetter(AttributeInfo::attributeModifier)
        ).apply(instance, AttributeInfo::new));
    }
}
