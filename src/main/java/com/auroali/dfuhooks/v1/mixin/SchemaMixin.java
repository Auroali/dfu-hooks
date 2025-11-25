package com.auroali.dfuhooks.v1.mixin;

import com.auroali.dfuhooks.v1.api.DFUHooks;
import com.auroali.dfuhooks.v1.api.SchemaRegistry;
import com.auroali.dfuhooks.v1.impl.SchemaRegistryImpl;
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
        if(SchemaRegistryImpl.SCHEMA_REGISTRY == null)
            return original;
        SchemaRegistryImpl.TypeRegistryBuilder builder = SchemaRegistryImpl.SCHEMA_REGISTRY.getEntities(this.getVersionKey());
        if(builder != null)
            builder.registerAll((Schema)(Object)this, original);
        return original;
    }
    @ModifyReturnValue(method = "registerBlockEntities", at = @At("RETURN"))
    public Map<String, Supplier<TypeTemplate>> dfuhooks$blockEntityRegistryHook(Map<String, Supplier<TypeTemplate>> original) {
        if(SchemaRegistryImpl.SCHEMA_REGISTRY == null)
            return original;
        SchemaRegistryImpl.TypeRegistryBuilder builder = SchemaRegistryImpl.SCHEMA_REGISTRY.getBlockEntities(this.getVersionKey());
        if(builder != null)
            builder.registerAll((Schema)(Object)this, original);
        return original;
    }

    @Inject(method = "registerTypes", at = @At("RETURN"))
    public void dfuhooks$registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes, CallbackInfo ci) {
        if(SchemaRegistryImpl.SCHEMA_REGISTRY == null)
            return;
        SchemaRegistryImpl.TypeReferenceRegistry registry = SchemaRegistryImpl.SCHEMA_REGISTRY.getTypeReferenceRegistry(this.getVersionKey());
        if(registry != null)
            registry.applyTo(schema);
    }
}
