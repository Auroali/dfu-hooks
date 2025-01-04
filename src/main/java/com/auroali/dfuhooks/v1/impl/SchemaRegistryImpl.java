package com.auroali.dfuhooks.v1.impl;

import com.auroali.dfuhooks.v1.api.DFUHooks;
import com.auroali.dfuhooks.v1.api.SchemaRegistry;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SchemaRegistryImpl implements SchemaRegistry {
    public static SchemaRegistryImpl SCHEMA_REGISTRY;
    public static final Logger LOGGER = LoggerFactory.getLogger("DFU Hooks");

    protected Map<Integer, TypeRegistryBuilder> schemaEntities = new HashMap<>();
    protected Map<Integer, TypeRegistryBuilder> schemaBlockEntities = new HashMap<>();
    protected Map<Integer, TypeReferenceRegistry> types = new HashMap<>();

    @Override
    public void registerEntity(int version, int subVersion, String name, SchemaRegistry.TypeTemplateSupplier builder) {
        schemaEntities.computeIfAbsent(
                DataFixUtils.makeKey(version, subVersion),
                        i -> new TypeRegistryBuilder()
                )
                .add(name, builder);
    }

    @Override
    public void registerBlockEntity(int version, int subVersion, String name, SchemaRegistry.TypeTemplateSupplier builder) {
        schemaBlockEntities.computeIfAbsent(
                DataFixUtils.makeKey(version, subVersion),
                        i -> new TypeRegistryBuilder()
        ).add(name, builder);
    }

    @Override
    public void registerType(int version, int subversion, boolean recursive, DSL.TypeReference reference, SchemaRegistry.TypeTemplateSupplier typeTemplate) {
        this.types.computeIfAbsent(
                DataFixUtils.makeKey(version, subversion),
                        v -> new TypeReferenceRegistry()
                )
                .register(recursive, reference, typeTemplate);
    }

    public TypeRegistryBuilder getEntities(int versionKey) {
        return schemaEntities.remove(versionKey);
    }

    public TypeRegistryBuilder getBlockEntities(int versionKey) {
        return schemaBlockEntities.remove(versionKey);
    }

    public TypeReferenceRegistry getTypeReferenceRegistry(int versionKey) {
        return this.types.remove(versionKey);
    }

    public void validate() {
        if(!schemaEntities.isEmpty()) {
            schemaEntities.forEach((versionKey, builder) -> {
                int version = DataFixUtils.getVersion(versionKey);
                int subVersion = DataFixUtils.getSubVersion(versionKey);
                String versionString = "V" + version + (subVersion == 0 ? "" : "." + subVersion);
                for(String name : builder.entries.keySet()) {
                    LOGGER.warn("Failed to register datafixer for entity \"{}\" for schema {}", name, versionString);
                }
            });
            if(FabricLoader.getInstance().isDevelopmentEnvironment())
                throw new IllegalStateException("Failed to register one or more entity datafixers");
        }
        if(!schemaBlockEntities.isEmpty()) {
            schemaBlockEntities.forEach((versionKey, builder) -> {
                int version = DataFixUtils.getVersion(versionKey);
                int subVersion = DataFixUtils.getSubVersion(versionKey);
                String versionString = "V" + version + (subVersion == 0 ? "" : "." + subVersion);
                for(String name : builder.entries.keySet()) {
                    LOGGER.warn("Failed to register datafixer for block entity \"{}\" for schema {}", name, versionString);
                }
            });
            if(FabricLoader.getInstance().isDevelopmentEnvironment())
                throw new IllegalStateException("Failed to register one or more block entity datafixers");
        }
        if(!types.isEmpty()) {
            types.forEach((versionKey, registry) -> {
                int version = DataFixUtils.getVersion(versionKey);
                int subVersion = DataFixUtils.getSubVersion(versionKey);
                String versionString = "V" + version + (subVersion == 0 ? "" : "." + subVersion);
                LOGGER.warn("Failed to register type for schema {}", versionString);
            });
            if(FabricLoader.getInstance().isDevelopmentEnvironment())
                throw new IllegalStateException("Failed to register one or more types");
        }
    }

    public static class TypeRegistryBuilder {
        protected Map<String, SchemaRegistry.TypeTemplateSupplier> entries = new HashMap<>();

        protected void add(String name, SchemaRegistry.TypeTemplateSupplier templateBuilder) {
            entries.put(name, templateBuilder);
        }

        public void registerAll(Schema schema, Map<String, Supplier<TypeTemplate>> map) {
            for(Map.Entry<String, SchemaRegistry.TypeTemplateSupplier> entry : this.entries.entrySet()) {
                schema.register(map, entry.getKey(), entry.getValue().create(schema));
            }
        }
    }

    public static class TypeReferenceRegistry {
        List<Entry> entries = new ArrayList<>();

        public void register(boolean recursive, DSL.TypeReference reference, SchemaRegistry.TypeTemplateSupplier typeTemplate) {
            new Entry(recursive, reference, typeTemplate);
        }

        public void applyTo(Schema schema) {
            for(Entry entry : this.entries) {
                schema.registerType(entry.recursive, entry.typeReference, entry.typeTemplate.create(schema));
            }
        }

        protected class Entry {
            final boolean recursive;
            final DSL.TypeReference typeReference;
            final SchemaRegistry.TypeTemplateSupplier typeTemplate;
            public Entry(boolean recursive, DSL.TypeReference typeReference, SchemaRegistry.TypeTemplateSupplier typeTemplate) {
                this.recursive = recursive;
                this.typeReference = typeReference;
                this.typeTemplate = typeTemplate;
                TypeReferenceRegistry.this.entries.add(this);
            }
        }
    }
}
