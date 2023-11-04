package com.pancake.surviving_the_aftermath.api;

import com.google.gson.JsonElement;

public interface IJSONSerializable {
    void deserializeJson(JsonElement jsonElement);
    JsonElement serializeJson();
}