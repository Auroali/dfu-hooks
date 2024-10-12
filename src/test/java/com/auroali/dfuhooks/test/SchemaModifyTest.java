package com.auroali.dfuhooks.test;

import com.auroali.dfuhooks.DFUHooksSchemaHook;
import com.auroali.dfuhooks.builtinfixes.DFUHooksItemIdFix;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixerBuilder;

public class SchemaModifyTest implements DFUHooksSchemaHook {
    @Override
    public void modifySchemas(DataFixerBuilder builder, SchemaGetter schemas) {
        // get the schema for version 3818 subversion 5
        // this is the schema used for the item stack componentization fix
        schemas.fromVersion(3818, 5).ifPresent(schema -> {
            // adds a fixer that replaces all diamonds with emeralds
            builder.addFixer(new DFUHooksItemIdFix(
                    schema,
                    "dfuhooks-test",
                    ImmutableMap
                            .<String, String>builder()
                            .put("minecraft:diamond", "minecraft:emerald")
                            .build()
            ));
        });

    }
}
