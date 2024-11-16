package com.auroali.dfuhooks.mixin;

import com.auroali.dfuhooks.DFUHooks;
import com.auroali.dfuhooks.SchemaRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.datafixer.schema.Schema99;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.function.Supplier;

// needs a mixin here because Schema99 doesn't call the super method
@Mixin(Schema99.class)
public abstract class Schema99Mixin extends Schema {

    public Schema99Mixin(int versionKey, Schema parent) {
        super(versionKey, parent);
    }

    @ModifyReturnValue(method = "registerEntities", at = @At("RETURN"), remap = false)
    public Map<String, Supplier<TypeTemplate>> dfuhooks$entityRegistryHook(Map<String, Supplier<TypeTemplate>> original) {
        SchemaRegistry.TypeRegistryBuilder builder = DFUHooks.SCHEMA_REGISTRY.getEntities(this.getVersionKey());
        if(builder != null)
            builder.registerAll(this, original);
        return original;
    }
    @ModifyReturnValue(method = "registerBlockEntities", at = @At("RETURN"), remap = false)
    public Map<String, Supplier<TypeTemplate>> dfuhooks$blockEntityRegistryHook(Map<String, Supplier<TypeTemplate>> original) {
        SchemaRegistry.TypeRegistryBuilder builder = DFUHooks.SCHEMA_REGISTRY.getBlockEntities(this.getVersionKey());
        if(builder != null)
            builder.registerAll(this, original);
        return original;
    }
}
