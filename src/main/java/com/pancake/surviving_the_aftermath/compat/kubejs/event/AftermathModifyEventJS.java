package com.pancake.surviving_the_aftermath.compat.kubejs.event;

import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

public class AftermathModifyEventJS extends EventJS {
    private final ResourceLocation identifier;
    private final Collection<IAftermathModule> aftermathModules;

    public AftermathModifyEventJS(ResourceLocation identifier, Collection<IAftermathModule> aftermathModules) {
        this.identifier = identifier;
        this.aftermathModules = aftermathModules;
    }

    public ResourceLocation getIdentifier() {
        return identifier;
    }

    public Collection<IAftermathModule> getAftermathModules() {
        return aftermathModules;
    }

    public void remove(String name) {
        aftermathModules.removeIf(aftermathModule -> aftermathModule.getModuleName().equals(name));
    }

    public void add(IAftermathModule aftermathModule) {
        aftermathModules.add(aftermathModule);
    }
}