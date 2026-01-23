package com.auroali.dfuhooks.v2.mixin;

import com.auroali.dfuhooks.v2.api.SchemaBuilder;
import com.auroali.dfuhooks.v2.impl.DFUHooks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@ApiStatus.Internal
@Mixin(Schema.class)
public class SchemaMixin {
    @Shadow
    @Final
    private int versionKey;

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/schemas/Schema;registerEntities(Lcom/mojang/datafixers/schemas/Schema;)Ljava/util/Map;"))
    public Map<String, Supplier<TypeTemplate>> dfuhooks$registerEntities(Schema instance, Schema schema, Operation<Map<String, Supplier<TypeTemplate>>> original) {
        HashMap<Integer, SchemaBuilder> builders = DFUHooks.BUILDERS.get();
        Map<String, Supplier<TypeTemplate>> types = original.call(instance, schema);
        if (builders != null && builders.containsKey(instance.getVersionKey())) {
            builders
              .get(instance.getVersionKey())
              .buildEntities()
              .forEach((id, func) -> {
                  types.put(id, () -> func.accept(schema));
                  DFUHooks.LOGGER.debug("Registered entity {} in schema {}", id, this.versionKey);
              });
        }
        return types;
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/datafixers/schemas/Schema;registerBlockEntities(Lcom/mojang/datafixers/schemas/Schema;)Ljava/util/Map;"))
    public Map<String, Supplier<TypeTemplate>> dfuhooks$registerBlockEntities(Schema instance, Schema schema, Operation<Map<String, Supplier<TypeTemplate>>> original) {
        HashMap<Integer, SchemaBuilder> builders = DFUHooks.BUILDERS.get();
        Map<String, Supplier<TypeTemplate>> types = original.call(instance, schema);
        if (builders != null && builders.containsKey(instance.getVersionKey())) {
            builders
              .get(instance.getVersionKey())
              .buildBlockEntities()
              .forEach((id, func) -> {
                  types.put(id, () -> func.accept(schema));
                  DFUHooks.LOGGER.debug("Registered block entity {} in schema {}", id, this.versionKey);
              });
        }
        return types;
    }
}
