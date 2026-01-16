package com.auroali.dfuhooks.v2.impl;

import com.auroali.dfuhooks.v2.api.SchemaBuilder;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DefaultSchemaBuilder implements SchemaBuilder {
    private final HashMap<String, Supplier<TypeTemplate>> entityTemplates;
    private final HashMap<String, Supplier<TypeTemplate>> blockEntityTemplates;
    private final List<Function<Schema, DataFix>> fixers;

    public DefaultSchemaBuilder() {
        this.entityTemplates = new HashMap<>();
        this.blockEntityTemplates = new HashMap<>();
        this.fixers = new ArrayList<>();
    }

    @Override
    public void addFixer(Function<Schema, DataFix> fix) {
        this.fixers.add(fix);
    }

    @Override
    public void registerEntity(String id, Supplier<TypeTemplate> template) {
        if (this.entityTemplates.containsKey(id))
            throw new IllegalArgumentException(id + " is already registered as an entity template");
        this.entityTemplates.put(id, template);
    }

    @Override
    public void registerBlockEntity(String id, Supplier<TypeTemplate> template) {
        if (this.blockEntityTemplates.containsKey(id))
            throw new IllegalArgumentException(id + " is already registered as a block entity template");
        this.blockEntityTemplates.put(id, template);
    }

    @Override
    public Stream<DataFix> buildFixers(Schema schema) {
        return this.fixers.stream().map(builder -> builder.apply(schema));
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> buildEntities() {
        return this.entityTemplates;
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> buildBlockEntities() {
        return this.blockEntityTemplates;
    }
}
