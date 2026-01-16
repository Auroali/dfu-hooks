package com.auroali.dfuhooks.v2.mixin;

import com.auroali.dfuhooks.v2.api.ModDFUInitializer;
import com.auroali.dfuhooks.v2.api.SchemaBuilder;
import com.auroali.dfuhooks.v2.impl.DFUHooks;
import com.auroali.dfuhooks.v2.impl.DataFixerBuilderExt;
import com.auroali.dfuhooks.v2.impl.DefaultSchemaBuilder;
import com.mojang.datafixers.DataFixerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.datafix.DataFixers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(DataFixers.class)
public class DataFixersMixin {
    @Inject(method = "addFixers", at = @At("HEAD"))
    private static void dfuhooks$setupBuilders(DataFixerBuilder dataFixerBuilder, CallbackInfo ci) {
        if (dataFixerBuilder instanceof DataFixerBuilderExt ext)
            ext.dfuhooks$setVanilla();

        DFUHooks.BUILDERS.set(new HashMap<>());
        FabricLoader.getInstance().invokeEntrypoints(
          "dfuhooks-init",
          ModDFUInitializer.class,
          init -> {
              init.init();
              init.getInitializers().forEach((version, schemaInit) -> {
                  SchemaBuilder builder = DFUHooks.BUILDERS.get().computeIfAbsent(version, k -> new DefaultSchemaBuilder());
                  schemaInit.initSchemaBuilder(builder);
              });
          }
        );
    }

    @Inject(method = "addFixers", at = @At("RETURN"))
    private static void dfuhooks$cleanupBuilders(DataFixerBuilder dataFixerBuilder, CallbackInfo ci) {
        DFUHooks.BUILDERS.remove();
    }
}
