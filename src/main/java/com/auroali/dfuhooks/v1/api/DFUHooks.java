package com.auroali.dfuhooks.v1.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;

public class DFUHooks {
    public static NbtCompound update(DataFixer dataFixer, DSL.TypeReference type, NbtCompound tag, int oldVersion, int currentVersion) {
        return (NbtCompound) dataFixer.update(type, new Dynamic<>(NbtOps.INSTANCE, tag), oldVersion, currentVersion).getValue();
    }

    public static NbtCompound update(DataFixer dataFixer, DSL.TypeReference type, NbtCompound tag, int oldVersion) {
        return update(dataFixer, type, tag, oldVersion, SharedConstants.getGameVersion().getSaveVersion().getId());
    }

    public static JsonElement update(DataFixer dataFixer, DSL.TypeReference type, JsonElement json, int oldVersion, int currentVersion) {
        return dataFixer.update(type, new Dynamic<>(JsonOps.INSTANCE, json), oldVersion, currentVersion).getValue();
    }

    public static JsonElement update(DataFixer dataFixer, DSL.TypeReference type, JsonElement json, int oldVersion) {
        return update(dataFixer, type, json, oldVersion, SharedConstants.getGameVersion().getSaveVersion().getId());
    }

    public static JsonObject update(DataFixer dataFixer, DSL.TypeReference type, JsonObject json, int oldVersion, int currentVersion) {
        return (JsonObject) update(dataFixer, type, (JsonElement) json, oldVersion, currentVersion);
    }

    public static JsonObject update(DataFixer dataFixer, DSL.TypeReference type, JsonObject json, int oldVersion) {
        return update(dataFixer, type, json, oldVersion, SharedConstants.getGameVersion().getSaveVersion().getId());
    }
}
