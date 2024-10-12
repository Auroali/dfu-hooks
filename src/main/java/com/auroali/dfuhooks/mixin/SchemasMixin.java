package com.auroali.dfuhooks.mixin;

import com.auroali.dfuhooks.DFUHooksSchemaHook;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.datafixer.Schemas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Schemas.class)
public abstract class SchemasMixin {

    @Inject(method = "build", at = @At("TAIL"))
    private static void dfuhooks$registerAndModifySchemas(DataFixerBuilder builder, CallbackInfo ci) {
        Int2ObjectSortedMap<Schema> schemas = ((DataFixerBuilderAccessor)builder).getSchemas();
        FabricLoader.getInstance().invokeEntrypoints(
                "dfuhooks-modify-schemas",
                DFUHooksSchemaHook.class,
                hook -> hook.modifySchemas(builder, new DFUHooksSchemaHook.SchemaGetter(schemas))
        );
    }
}
