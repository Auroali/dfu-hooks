package com.auroali.dfuhooks;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SchemaRegistry {
    protected Map<Integer, TypeRegistryBuilder> schemaEntities = new HashMap<>();
    protected Map<Integer, TypeRegistryBuilder> schemaBlockEntities = new HashMap<>();

    /**
     * Registers an entity datafixer for the given schema
     * @param version the schema's version
     * @param subVersion the schema's subversion
     * @param name the name (id) of the entity (ex. "minecraft:pig")
     * @param builder the builder for the type template supplier
     */
    public void registerEntity(int version, int subVersion, String name, TypeTemplateSupplier builder) {
        schemaEntities.computeIfAbsent(DataFixUtils.makeKey(version, subVersion), i -> new TypeRegistryBuilder())
                .add(name, builder);
    }

    /**
     * Registers an entity datafixer for the given schema
     * @param version the schema's version
     * @param subVersion the schema's subversion
     * @param name the name (id) of the entity (ex. "minecraft:pig")
     */
    public void registerEntitySimple(int version, int subVersion, String name) {
        schemaEntities.computeIfAbsent(DataFixUtils.makeKey(version, subVersion), i -> new TypeRegistryBuilder())
                .add(name, schema -> DSL::remainder);
    }

    /**
     * Registers an entity datafixer for the given schema
     * @param version the schema's version
     * @param name the name (id) of the entity (ex. "minecraft:pig")
     * @param builder the builder for the type template supplier
     */
    public void registerEntity(int version, String name, TypeTemplateSupplier builder) {
        registerEntity(version, 0, name, builder);
    }

    /**
     * Registers an entity datafixer for the given schema
     * @param version the schema's version
     * @param name the name (id) of the entity (ex. "minecraft:pig")
     */
    public void registerEntitySimple(int version, String name) {
        registerEntitySimple(version, 0, name);
    }

    /**
     * Registers a block entity datafixer for the given schema
     * @param version the schema's version
     * @param subVersion the schema's subversion
     * @param name the name (id) of the block entity (ex. "minecraft:chest")
     * @param builder the builder for the type template supplier
     */
    public void registerBlockEntity(int version, int subVersion, String name, TypeTemplateSupplier builder) {
        schemaBlockEntities.computeIfAbsent(DataFixUtils.makeKey(version, subVersion), i -> new TypeRegistryBuilder())
                .add(name, builder);
    }

    /**
     * Registers a block entity datafixer for the given schema
     * @param version the schema's version
     * @param subVersion the schema's subversion
     * @param name the name (id) of the block entity (ex. "minecraft:chest")
     */
    public void registerBlockEntitySimple(int version, int subVersion, String name) {
        schemaBlockEntities.computeIfAbsent(DataFixUtils.makeKey(version, subVersion), i -> new TypeRegistryBuilder())
                .add(name, schema -> DSL::remainder);
    }

    /**
     * Registers a block entity datafixer for the given schema
     * @param version the schema's version
     * @param name the name (id) of the block entity (ex. "minecraft:chest")
     * @param builder the builder for the type template supplier
     */
    public void registerBlockEntity(int version, String name, TypeTemplateSupplier builder) {
        registerBlockEntity(version, 0, name, builder);
    }

    /**
     * Registers a block entity datafixer for the given schema
     * @param version the schema's version
     * @param name the name (id) of the block entity (ex. "minecraft:chest")
     */
    public void registerBlockEntitySimple(int version, String name) {
        registerBlockEntitySimple(version, 0, name);
    }

    /**
     * Do not use! Use registerEntity instead
     */
    public TypeRegistryBuilder getEntities(int versionKey) {
        return schemaEntities.remove(versionKey);
    }

    /**
     * Do not use! Use registerBlockEntity instead
     */
    public TypeRegistryBuilder getBlockEntities(int versionKey) {
        return schemaBlockEntities.remove(versionKey);
    }

    public void validate() {
        if(!schemaEntities.isEmpty()) {
            schemaEntities.forEach((versionKey, builder) -> {
                int version = DataFixUtils.getVersion(versionKey);
                int subVersion = DataFixUtils.getSubVersion(versionKey);
                String versionString = "V" + version + (subVersion == 0 ? "" : "." + subVersion);
                for(String name : builder.entries.keySet()) {
                    DFUHooks.LOGGER.warn("Failed to register datafixer for entity \"{}\" for schema {}", name, versionString);
                }
            });
        }
        if(!schemaBlockEntities.isEmpty()) {
            schemaBlockEntities.forEach((versionKey, builder) -> {
                int version = DataFixUtils.getVersion(versionKey);
                int subVersion = DataFixUtils.getSubVersion(versionKey);
                String versionString = "V" + version + (subVersion == 0 ? "" : "." + subVersion);
                for(String name : builder.entries.keySet()) {
                    DFUHooks.LOGGER.warn("Failed to register datafixer for block entity \"{}\" for schema {}", name, versionString);
                }
            });
        }
    }

    public static class TypeRegistryBuilder {
        protected Map<String, TypeTemplateSupplier> entries = new HashMap<>();

        protected void add(String name, TypeTemplateSupplier templateBuilder) {
            entries.put(name, templateBuilder);
        }

        public void registerAll(Schema schema, Map<String, Supplier<TypeTemplate>> map) {
            for(Map.Entry<String, TypeTemplateSupplier> entry : this.entries.entrySet()) {
                schema.register(map, entry.getKey(), entry.getValue().create(schema));
            }
        }
    }

    public interface TypeTemplateSupplier {
        Supplier<TypeTemplate> create(Schema schema);
    }
}
