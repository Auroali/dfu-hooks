package com.auroali.dfuhooks;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import it.unimi.dsi.fastutil.Function;

import java.util.Optional;

public interface DFUHooksSchemaHook {
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
