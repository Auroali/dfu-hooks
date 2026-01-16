package com.auroali.dfuhooks.v2.api;

@FunctionalInterface
public interface SchemaInitializer {
    void initSchemaBuilder(SchemaBuilder builder);
}
