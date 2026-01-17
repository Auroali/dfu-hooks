package com.auroali.dfuhooks.v2.api;

/**
 * Functional interface to consume a SchemaBuilder
 *
 * @author Auroali
 * @since 2.0.0
 */
@FunctionalInterface
public interface SchemaBuilderConsumer {
    void acceptSchemaBuilder(SchemaBuilder builder);
}
