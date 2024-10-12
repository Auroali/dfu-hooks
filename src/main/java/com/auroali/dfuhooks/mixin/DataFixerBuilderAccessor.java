package com.auroali.dfuhooks.mixin;

import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DataFixerBuilder.class)
public interface DataFixerBuilderAccessor {
    @Accessor("schemas")
    Int2ObjectSortedMap<Schema> getSchemas();
}
