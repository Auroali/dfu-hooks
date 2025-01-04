package com.auroali.dfuhooks.test;

import com.auroali.dfuhooks.v1.api.DFUHooksSchemaHook;
import com.auroali.dfuhooks.v1.api.SchemaRegistry;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixerBuilder;
import net.minecraft.datafixer.Schemas;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.datafixer.fix.BlockNameFix;
import net.minecraft.datafixer.fix.ItemNameFix;
import net.minecraft.datafixer.schema.Schema100;

public class SchemaModifyTest implements DFUHooksSchemaHook {
    @Override
    public void register(SchemaRegistry registry) {
        registry.registerEntitySimple(3818, 5, "dfuhooks:test_simple");
        registry.registerBlockEntitySimple(3818, 5, "dfuhooks:test_be_simple");
        registry.registerEntity(
                3818,
                5,
                "dfuhooks:test",
                schema -> () -> Schema100.targetItems(schema)
        );
        registry.registerBlockEntity(
                3818,
                5,
                "dfuhooks:test_be",
                schema -> () -> DSL.optionalFields("Items", DSL.list(TypeReferences.ITEM_STACK.in(schema)))
        );
    }

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
}
