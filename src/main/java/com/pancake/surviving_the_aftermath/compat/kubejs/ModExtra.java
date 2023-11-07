package com.pancake.surviving_the_aftermath.compat.kubejs;

import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import dev.latvian.mods.kubejs.event.Extra;

import java.util.List;

public class ModExtra{
    protected static final AftermathAPI API = AftermathAPI.getInstance();

    public static final Extra AFTERMATH_TYPE = new Extra().transformer(ModExtra::toRegistryAftermath);


    private static List<IAftermathModule> toRegistryAftermath(Object object){
        if (object == null) {
            return null;
        }
        if (object instanceof String str) {
            return API.getAftermathModules(str);
        }
        return null;
    }
}
