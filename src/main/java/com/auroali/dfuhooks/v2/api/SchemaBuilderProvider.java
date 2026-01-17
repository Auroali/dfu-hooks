package com.auroali.dfuhooks.v2.api;

/**
 * Provides methods to initialize SchemaBuilders during
 * DFU setup
 *
 * @author Auroali
 * @since 2.0.0
 */
@FunctionalInterface
public interface SchemaBuilderProvider {
    void createOrModifySchemaBuilder(int version, int subversion, SchemaBuilderConsumer consumer);

    default void createOrModifySchemaBuilder(int version, SchemaBuilderConsumer consumer) {
        this.createOrModifySchemaBuilder(version, 0, consumer);
    }
}
