package com.auroali.dfuhooks;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import it.unimi.dsi.fastutil.Function;

import java.util.Optional;

public interface DFUHooksSchemaHook {
    /**
     * Allows registering block entities and entities, plus adding schemas
     * @param registry the schema registry
     */
    void register(SchemaRegistry registry);

    /**
     * Allows registering custom datafixers
     * <br> Already registered schemas can get gotten via fromVersion in SchemaGetter, specific version keys can
     * be found in the Schemas class
     * @param builder the data fixer builder
     * @param schemas getter for existing schemas
     * @see net.minecraft.datafixer.Schemas
     */
    void modifySchemas(DataFixerBuilder builder, SchemaGetter schemas);

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
