package com.auroali.dfuhooks.test;

import com.auroali.dfuhooks.DFUHooksSchemaHook;
import com.mojang.datafixers.DataFixerBuilder;

public class SchemaModifyTest implements DFUHooksSchemaHook {
    @Override
    public void modifySchemas(DataFixerBuilder builder, SchemaGetter schemas) {
        System.out.println("Test");
        schemas.fromVersion(99).ifPresent(schema -> {
            System.out.println("Found schema for dataversion 99");
            System.out.println(schema.getVersionKey());
        });

    }
}
