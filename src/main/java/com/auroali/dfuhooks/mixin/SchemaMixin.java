package com.auroali.dfuhooks.mixin;

import com.auroali.dfuhooks.DFUHooks;
import com.auroali.dfuhooks.SchemaRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(value = Schema.class, remap = false)
public abstract class SchemaMixin {
    @Shadow public abstract int getVersionKey();

    @ModifyReturnValue(method = "registerEntities", at = @At("RETURN"))
    public Map<String, Supplier<TypeTemplate>> dfuhooks$entityRegistryHook(Map<String, Supplier<TypeTemplate>> original) {
        SchemaRegistry.TypeRegistryBuilder builder = DFUHooks.SCHEMA_REGISTRY.getEntities(this.getVersionKey());
        if(builder != null)
            builder.registerAll((Schema)(Object)this, original);
        return original;
    }
    @ModifyReturnValue(method = "registerBlockEntities", at = @At("RETURN"))
    public Map<String, Supplier<TypeTemplate>> dfuhooks$blockEntityRegistryHook(Map<String, Supplier<TypeTemplate>> original) {
        SchemaRegistry.TypeRegistryBuilder builder = DFUHooks.SCHEMA_REGISTRY.getBlockEntities(this.getVersionKey());
        if(builder != null)
            builder.registerAll((Schema)(Object)this, original);
        return original;
    }

    @Inject(method = "registerTypes", at = @At("RETURN"))
    public void dfuhooks$registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes, CallbackInfo ci) {
        SchemaRegistry.TypeReferenceRegistry registry = DFUHooks.SCHEMA_REGISTRY.getTypeReferenceRegistry(this.getVersionKey());
        if(registry != null)
            registry.applyTo(schema);
    }
}
