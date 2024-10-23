package com.auroali.dfuhooks.mixin;

import com.auroali.dfuhooks.DFUHooksSchemaHook;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(Schema.class)
public class SchemaMixin {
    @Shadow(remap = false) @Final private int versionKey;

    @ModifyReturnValue(method = "registerEntities", at = @At("RETURN"), remap = false)
    public Map<String, Supplier<TypeTemplate>> dfuhooks$entityRegistryHook(Map<String, Supplier<TypeTemplate>> original) {
        FabricLoader.getInstance().invokeEntrypoints(
                "dfuhooks-modify-schemas",
                DFUHooksSchemaHook.class,
                hook -> hook.registerEntities(versionKey, (Schema) (Object) this, original)
        );
        return original;
    }
    @ModifyReturnValue(method = "registerBlockEntities", at = @At("RETURN"), remap = false)
    public Map<String, Supplier<TypeTemplate>> dfuhooks$blockEntityRegistryHook(Map<String, Supplier<TypeTemplate>> original) {
        FabricLoader.getInstance().invokeEntrypoints(
                "dfuhooks-modify-schemas",
                DFUHooksSchemaHook.class,
                hook -> hook.registerBlockEntities(versionKey, (Schema) (Object) this, original)
        );
        return original;
    }
}
