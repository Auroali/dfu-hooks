package com.auroali.dfuhooks.v2.mixin;

import com.auroali.dfuhooks.v2.api.ModDFUInitializer;
import com.auroali.dfuhooks.v2.api.SchemaBuilder;
import com.auroali.dfuhooks.v2.impl.DFUHooks;
import com.auroali.dfuhooks.v2.impl.DataFixerBuilderExt;
import com.auroali.dfuhooks.v2.impl.DefaultSchemaBuilder;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.filefix.FileFixerUpper;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@ApiStatus.Internal
@Mixin(DataFixers.class)
public class DataFixersMixin {
    @Inject(method = "addFixers", at = @At("HEAD"))
    private static void dfuhooks$setupBuilders(DataFixerBuilder fixerUpper, FileFixerUpper.Builder fileFixerUpper, CallbackInfo ci) {
        if (fixerUpper instanceof DataFixerBuilderExt ext)
            ext.dfuhooks$setVanilla();

        DFUHooks.BUILDERS.set(new HashMap<>());
        FabricLoader.getInstance().invokeEntrypoints(
          "dfuhooks-init",
          ModDFUInitializer.class,
          init -> {
              init.init((version, subversion, consumer) -> {
                  SchemaBuilder builder = DFUHooks.BUILDERS.get().computeIfAbsent(
                    DataFixUtils.makeKey(version, subversion),
                    k -> new DefaultSchemaBuilder()
                  );
                  consumer.acceptSchemaBuilder(builder);
              });
          }
        );
        DFUHooks.LOGGER.debug("Initialized {} builders from {} entrypoints",
          DFUHooks.BUILDERS.get().size(),
          FabricLoader.getInstance().getEntrypointContainers("dfuhooks-init", ModDFUInitializer.class).size()
        );
    }

    @Inject(method = "addFixers", at = @At("RETURN"))
    private static void dfuhooks$cleanupBuilders(DataFixerBuilder fixerUpper, FileFixerUpper.Builder fileFixerUpper, CallbackInfo ci) {
        DFUHooks.BUILDERS.remove();
        DFUHooks.LOGGER.debug("Successfully cleared builder map");
    }
}
