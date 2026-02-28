package com.auroali.dfuhooks.test;

import com.auroali.dfuhooks.v2.api.ModDFUInitializer;
import com.auroali.dfuhooks.v2.api.SchemaBuilder;
import com.auroali.dfuhooks.v2.api.SchemaBuilderProvider;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.ItemRenameFix;
import net.minecraft.util.datafix.fixes.References;

public class SchemaModifyTest extends ModDFUInitializer {
    @Override
    public void init(SchemaBuilderProvider provider) {
        provider.createOrModifySchemaBuilder(
          3818, 5,
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

        provider.createOrModifySchemaBuilder(4661, builder -> {
            builder.registerForTarget(
              SchemaBuilder.Target.ENTITY,
              Identifier.fromNamespaceAndPath("dfuhooks-test", "test"),
              schema -> DSL.optionalFields("additional_items", References.ITEM_STACK.in(schema))
            );

            builder.registerExtension(
              SchemaBuilder.Target.ENTITY,
              "minecraft:skeleton",
              schema -> DSL.optional(
                DSL.field("test", DSL.list(References.ITEM_STACK.in(schema)))
              )
            );
        });
    }
}
