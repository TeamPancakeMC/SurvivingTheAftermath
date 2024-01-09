package com.pancake.surviving_the_aftermath.compat.kubejs.event;

import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import dev.latvian.mods.kubejs.event.EventJS;

import java.util.List;
import java.util.Set;

public class AftermathModifyEventJS extends EventJS {
    private final String identifier;
    private final Set<IAftermathModule> aftermathModules;

    public AftermathModifyEventJS(String identifier, Set<IAftermathModule> aftermathModules) {
        this.identifier = identifier;
        this.aftermathModules = aftermathModules;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Set<IAftermathModule> getAftermathModules() {
        return aftermathModules;
    }

    public void remove(String JsonName) {
        aftermathModules.removeIf(aftermathModule -> aftermathModule.getJsonName().equals(JsonName));
    }

    public void add(IAftermathModule aftermathModule) {
        aftermathModules.add(aftermathModule);
    }
}
