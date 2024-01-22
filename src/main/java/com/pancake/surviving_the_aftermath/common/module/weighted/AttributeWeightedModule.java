package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.util.CodecUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class AttributeWeightedModule extends BaseWeightedModule<AttributeWeightedModule.AttributeInfo>{
    public static final String IDENTIFIER = "attribute_weighted";

    public static final Codec<AttributeWeightedModule> CODEC = Codec.list(WeightedEntry.Wrapper.codec(AttributeInfo.CODEC))
            .xmap(AttributeWeightedModule::new, AttributeWeightedModule::getList);

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

    public static class Builder {
        private List<WeightedEntry.Wrapper<AttributeInfo>> attributes;

        public Builder add(Attribute attribute, AttributeModifier modifier, int weight){
            attributes.add(WeightedEntry.wrap(new AttributeInfo(attribute,modifier), weight));
            return this;
        }

        public Builder add(String attribute, String name, double amount, int operation, int weight){
            attributes.add(WeightedEntry.wrap(new AttributeInfo(ForgeRegistries.ATTRIBUTES.getValue(ResourceLocation.tryParse(attribute)),
                    new AttributeModifier(name, amount, AttributeModifier.Operation.fromValue(operation))),
                    weight)
            );
            return this;
        }

        public AttributeWeightedModule build() {
            return new AttributeWeightedModule(attributes);
        }
    }
}
