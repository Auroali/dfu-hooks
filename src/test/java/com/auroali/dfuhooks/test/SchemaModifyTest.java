package com.auroali.dfuhooks.test;

import com.auroali.dfuhooks.v2.api.ModDFUInitializer;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.ItemRenameFix;

public class SchemaModifyTest extends ModDFUInitializer {
    @Override
    public void init() {
        this.initializeSchemaBuilder(
                DataFixUtils.makeKey(3818, 5),
                builder -> {
                    builder.addFixer(schema ->
                            ItemRenameFix.create(
                                    schema,
                                    "DFU Hooks Example Item Fix",
                                    DataFixers.createRenamer(ImmutableMap.of(
                                                    "minecraft:diamond", "minecraft:emerald",
                                                    "minecraft:diamond_block", "minecraft:emerald_block"
                                            )
                                    )
                            )
                    );
                }
        );
    }
}
