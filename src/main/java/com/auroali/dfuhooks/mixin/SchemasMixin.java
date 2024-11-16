package com.auroali.dfuhooks.mixin;

import com.auroali.dfuhooks.DFUHooks;
import com.auroali.dfuhooks.DFUHooksSchemaHook;
import com.auroali.dfuhooks.SchemaRegistry;
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

    @Inject(method = "build", at = @At("HEAD"))
    private static void dfuhooks$initSchemaRegistry(DataFixerBuilder builder, CallbackInfo ci) {
        DFUHooks.SCHEMA_REGISTRY = new SchemaRegistry();
        FabricLoader.getInstance().invokeEntrypoints("dfuhooks-modify-schemas", DFUHooksSchemaHook.class, hook -> hook.register(DFUHooks.SCHEMA_REGISTRY));
    }

    @Inject(method = "build", at = @At("TAIL"))
    private static void dfuhooks$registerAndModifySchemas(DataFixerBuilder builder, CallbackInfo ci) {
        // print a message if we failed to register anything
        DFUHooks.SCHEMA_REGISTRY.validate();
        // we don't need the schema registry anymore
        DFUHooks.SCHEMA_REGISTRY = null;

        Int2ObjectSortedMap<Schema> schemas = ((DataFixerBuilderAccessor)builder).getSchemas();
        FabricLoader.getInstance().invokeEntrypoints(
                "dfuhooks-modify-schemas",
                DFUHooksSchemaHook.class,
                hook -> hook.modifySchemas(builder, new DFUHooksSchemaHook.SchemaGetter(schemas))
        );
    }
}
