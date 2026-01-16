package com.auroali.dfuhooks.v2.api;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface SchemaBuilder {
    void addFixer(Function<Schema, DataFix> fix);

    void registerEntity(String id, Supplier<TypeTemplate> template);

    void registerBlockEntity(String id, Supplier<TypeTemplate> template);

    Stream<DataFix> buildFixers(Schema schema);

    Map<String, Supplier<TypeTemplate>> buildEntities();

    Map<String, Supplier<TypeTemplate>> buildBlockEntities();

    default void addFixer(DataFix fix) {
        this.addFixer(schema -> fix);
    }

    default void registerEntitySimple(String id) {
        this.registerEntity(id, DSL::remainder);
    }

    default void registerBlockEntitySimple(String id) {
        this.registerBlockEntity(id, DSL::remainder);
    }

    default void registerEntity(Identifier id, Supplier<TypeTemplate> template) {
        this.registerEntity(id.toString(), template);
    }

    default void registerBlockEntity(Identifier id, Supplier<TypeTemplate> template) {
        this.registerBlockEntity(id.toString(), template);
    }

    default void registerEntitySimple(Identifier id) {
        this.registerEntitySimple(id.toString());
    }

    default void registerBlockEntitySimple(Identifier id) {
        this.registerBlockEntitySimple(id.toString());
    }
}
