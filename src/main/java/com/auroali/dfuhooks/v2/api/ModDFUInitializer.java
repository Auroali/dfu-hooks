package com.auroali.dfuhooks.v2.api;

import java.util.HashMap;
import java.util.Map;

public abstract class ModDFUInitializer {
    private final HashMap<Integer, SchemaInitializer> initializers;

    public ModDFUInitializer() {
        this.initializers = new HashMap<>();
    }

    public abstract void init();

    public void initializeSchemaBuilder(int versionKey, SchemaInitializer initializer) {
        if (this.initializers.containsKey(versionKey)) {
            this.initializers.computeIfPresent(
              versionKey,
              (key, init) -> (builder) -> {
                  init.initSchemaBuilder(builder);
                  initializer.initSchemaBuilder(builder);
              }
            );
        } else this.initializers.put(versionKey, initializer);
    }

    public Map<Integer, SchemaInitializer> getInitializers() {
        return this.initializers;
    }
}
