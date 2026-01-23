package com.auroali.dfuhooks.v2.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * Provides methods to initialize SchemaBuilders during
 * DFU setup
 *
 * @author Auroali
 * @since 2.0.0
 */
@ApiStatus.NonExtendable
@FunctionalInterface
public interface SchemaBuilderProvider {
    void createOrModifySchemaBuilder(int version, int subversion, SchemaBuilderConsumer consumer);

    default void createOrModifySchemaBuilder(int version, SchemaBuilderConsumer consumer) {
        this.createOrModifySchemaBuilder(version, 0, consumer);
    }
}
