package com.auroali.dfuhooks.v1.mixin;

import com.auroali.dfuhooks.v1.api.DFUHooks;
import com.auroali.dfuhooks.v1.api.DFUHooksSchemaHook;
import com.auroali.dfuhooks.v1.impl.SchemaRegistryImpl;
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
        SchemaRegistryImpl.SCHEMA_REGISTRY = new SchemaRegistryImpl();
        FabricLoader.getInstance().invokeEntrypoints("dfuhooks-modify-schemas", DFUHooksSchemaHook.class, hook -> hook.register(SchemaRegistryImpl.SCHEMA_REGISTRY));
    }

    @Inject(method = "build", at = @At("TAIL"))
    private static void dfuhooks$registerAndModifySchemas(DataFixerBuilder builder, CallbackInfo ci) {
        // print a message if we failed to register anything
        SchemaRegistryImpl.SCHEMA_REGISTRY.validate();
        // we don't need the schema registry anymore
        SchemaRegistryImpl.SCHEMA_REGISTRY = null;

        Int2ObjectSortedMap<Schema> schemas = ((DataFixerBuilderAccessor)builder).getSchemas();
        FabricLoader.getInstance().invokeEntrypoints(
                "dfuhooks-modify-schemas",
                DFUHooksSchemaHook.class,
                hook -> hook.modifySchemas(builder, new DFUHooksSchemaHook.SchemaGetter(schemas))
        );
    }
}
