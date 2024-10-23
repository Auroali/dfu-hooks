package com.auroali.dfuhooks;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import it.unimi.dsi.fastutil.Function;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public interface DFUHooksSchemaHook {
    /**
     * Allows adding schemas and fixers, as well as modifying existing schemas.
     * <br> Already registered schemas can get gotten via fromVersion in SchemaGetter, specific version keys can
     * be found in the Schemas class
     * <br> It's not recommended to add custom schemas, and in most scenarios should not be necessary
     * @param builder the data fixer builder
     * @param schemas getter for existing schemas
     * @see net.minecraft.datafixer.Schemas
     */
    void modifySchemas(DataFixerBuilder builder, SchemaGetter schemas);

    /**
     * Allows you to register entity datafixers
     * @param versionKey the version key for the schema this is being called for
     * @param schema the schema instance
     * @param map the map to register entities into
     * @see net.minecraft.datafixer.schema.Schema99#registerEntities(Schema)
     * @see net.minecraft.datafixer.schema.Schema3816#registerEntities(Schema)
     * @implNote  this is called for every schema, make sure to check if the version key matches the schema you want to modify
     */
    void registerEntities(int versionKey, Schema schema, Map<String, Supplier<TypeTemplate>> map);
    /**
     * Allows you to register block entity datafixers
     * @param versionKey the version key for the schema this is being called for
     * @param schema the schema instance
     * @param map the map to register block entities into
     * @see net.minecraft.datafixer.schema.Schema99#registerBlockEntities(Schema)
     * @see net.minecraft.datafixer.schema.Schema3689#registerBlockEntities(Schema)
     * @implNote  this is called for every schema, make sure to check if the version key matches the schema you want to modify
     */
    void registerBlockEntities(int versionKey, Schema schema, Map<String, Supplier<TypeTemplate>> map);

    class SchemaGetter {
        Function<Integer, Schema> schemaFunc;
        public SchemaGetter(Function<Integer, Schema> schemaFunc) {
            this.schemaFunc = schemaFunc;
        }
        public Optional<Schema> fromVersion(int version) {
            return fromVersion(version, 0);
        }
        public Optional<Schema> fromVersion(int version, int subversion) {
            Schema schema = schemaFunc.get(DataFixUtils.makeKey(version, subversion));
            return schema == null ? Optional.empty() : Optional.of(schema);
        }
    }
}
