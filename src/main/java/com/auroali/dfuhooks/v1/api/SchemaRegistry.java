package com.auroali.dfuhooks.v1.api;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import java.util.function.Supplier;

public interface SchemaRegistry {
    void registerEntity(int version, int subVersion, String name, TypeTemplateSupplier builder);

    default void registerEntitySimple(int version, int subVersion, String name) {
        registerEntity(version, subVersion, name, schema -> DSL::remainder);
    }

    /**
     * Registers an entity datafixer for the given schema
     * @param version the schema's version
     * @param name the name (id) of the entity (ex. "minecraft:pig")
     * @param builder the builder for the type template supplier
     */
    default void registerEntity(int version, String name, TypeTemplateSupplier builder) {
        registerEntity(version, 0, name, builder);
    }

    /**
     * Registers an entity datafixer for the given schema
     * @param version the schema's version
     * @param name the name (id) of the entity (ex. "minecraft:pig")
     */
    default void registerEntitySimple(int version, String name) {
        registerEntitySimple(version, 0, name);
    }

    /**
     * Registers a block entity datafixer for the given schema
     * @param version the schema's version
     * @param subVersion the schema's subversion
     * @param name the name (id) of the block entity (ex. "minecraft:chest")
     * @param builder the builder for the type template supplier
     */
    void registerBlockEntity(int version, int subVersion, String name, TypeTemplateSupplier builder);

    /**
     * Registers a block entity datafixer for the given schema
     * @param version the schema's version
     * @param subVersion the schema's subversion
     * @param name the name (id) of the block entity (ex. "minecraft:chest")
     */
    default void registerBlockEntitySimple(int version, int subVersion, String name) {
        registerBlockEntity(version, subVersion, name, schema -> DSL::remainder);
    }

    /**
     * Registers a block entity datafixer for the given schema
     * @param version the schema's version
     * @param name the name (id) of the block entity (ex. "minecraft:chest")
     * @param builder the builder for the type template supplier
     */
    default void registerBlockEntity(int version, String name, TypeTemplateSupplier builder) {
        registerBlockEntity(version, 0, name, builder);
    }

    /**
     * Registers a block entity datafixer for the given schema
     * @param version the schema's version
     * @param name the name (id) of the block entity (ex. "minecraft:chest")
     */
    default void registerBlockEntitySimple(int version, String name) {
        registerBlockEntitySimple(version, 0, name);
    }

    void registerType(int version, int subversion, boolean recursive, DSL.TypeReference reference, TypeTemplateSupplier typeTemplate);

    default void registerType(int version, DSL.TypeReference reference) {
        this.registerType(version, 0, false, reference, schema -> DSL::remainder);
    }
    default void registerType(int version, int subversion, DSL.TypeReference reference) {
        this.registerType(version, subversion, false, reference, schema -> DSL::remainder);
    }


    default void registerType(int version, boolean recursive, DSL.TypeReference reference, TypeTemplateSupplier typeTemplate) {
        this.registerType(version, 0, recursive, reference, typeTemplate);
    }


    interface TypeTemplateSupplier {
        Supplier<TypeTemplate> create(Schema schema);
    }
}
