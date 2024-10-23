package com.auroali.dfuhooks.test;

import com.auroali.dfuhooks.DFUHooksSchemaHook;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.datafixer.Schemas;
import net.minecraft.datafixer.fix.BlockNameFix;
import net.minecraft.datafixer.fix.ItemNameFix;

import java.util.Map;
import java.util.function.Supplier;

public class SchemaModifyTest implements DFUHooksSchemaHook {
    @Override
    public void modifySchemas(DataFixerBuilder builder, SchemaGetter schemas) {
        // get the schema for version 3818 subversion 5
        // this is the schema used for the item stack componentization fix
        schemas.fromVersion(3818, 5).ifPresent(schema -> {
            // adds a fixer that replaces all diamonds with emeralds and diamond blocks with emerald blocks
            builder.addFixer(ItemNameFix.create(
                    schema,
                    "DFU Hooks Example Item Fix",
                    Schemas.replacing(ImmutableMap.of(
                            "minecraft:diamond", "minecraft:emerald",
                            "minecraft:diamond_block", "minecraft:emerald_block"
                        )
                    )
            ));
            // adds a fixer that replaces diamond blocks with emerald blocks
            builder.addFixer(BlockNameFix.create(
                    schema,
                    "DFU Hooks Example Item Fix",
                    Schemas.replacing("minecraft:diamond_block", "minecraft:emerald_block")
            ));
        });

    }

    @Override
    public void registerEntities(int versionKey, Schema schema, Map<String, Supplier<TypeTemplate>> map) {

    }

    @Override
    public void registerBlockEntities(int versionKey, Schema schema, Map<String, Supplier<TypeTemplate>> map) {

    }
}
