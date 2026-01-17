package com.auroali.dfuhooks.v2.api;

/**
 * DFU Hooks mod entrypoint, called during DFU setup
 *
 * @author Auroali
 * @since 2.0.0
 */
public abstract class ModDFUInitializer {
    public abstract void init(SchemaBuilderProvider provider);
}
