package com.auroali.dfuhooks.v2.mixin;

import com.auroali.dfuhooks.v2.api.SchemaBuilder;
import com.auroali.dfuhooks.v2.impl.DFUHooks;
import com.auroali.dfuhooks.v2.impl.DataFixerBuilderExt;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@ApiStatus.Internal
@Mixin(DataFixerBuilder.class)
public abstract class DataFixerBuilderMixin implements DataFixerBuilderExt {
    @Shadow
    public abstract void addFixer(DataFix fix);

    @Unique
    private boolean dfuhooks$vanillaBuilder;

    @Override
    public boolean dfuhooks$isVanilla() {
        return this.dfuhooks$vanillaBuilder;
    }

    @Override
    public void dfuhooks$setVanilla() {
        this.dfuhooks$vanillaBuilder = true;
    }

    @Inject(method = "addSchema(Lcom/mojang/datafixers/schemas/Schema;)V", at = @At("RETURN"))
    public void dfuhooks$initSchemaFromBuilder(Schema schema, CallbackInfo ci) {
        if (!this.dfuhooks$isVanilla())
            return;
        HashMap<Integer, SchemaBuilder> builders = DFUHooks.BUILDERS.get();
        if (builders != null && builders.containsKey(schema.getVersionKey())) {
            builders.get(schema.getVersionKey())
              .buildDataFixers(schema)
              .forEach(this::addFixer);
        }
    }
}
