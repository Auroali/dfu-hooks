package com.auroali.dfuhooks.v2.api;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Builder for Minecraft's DFU Schemas. Provides a way to add additional
 * entities, block entities and DataFixes to a schema
 *
 * @author Auroali
 * @since 2.0.0
 */
public interface SchemaBuilder {
    /**
     * Adds a new fixer to the schema
     *
     * @param fix a function taking a {@link Schema} instance and returning a {@link DataFix}
     * @since 2.0.0
     */
    void addFixer(Function<Schema, DataFix> fix);

    /**
     * Registers a new entity definition
     *
     * <p>
     * Example:
     * <pre>{@code
     * SchemaBuilder builder = ...
     * // registers an entity definition that just passes data through via DSL::remainder
     * // equivalent to registerSimple
     * builder.registerEntity(
     *      "foo:bar",
     *      (name, schema) -> DSL::remainder
     * );
     * // registers an entity definition that specifies an optional item stack field
     * builder.registerEntity(
     *      "foo:bar",
     *      (name, schema) -> DSL.optionalFields("TestItem", References.ITEM_STACK.in(schema))
     * );
     * }</pre>
     *
     * @param id       the id of the entity, in {@link Identifier}'s string format
     * @param template a functional interface that takes a {@link Schema} and returns a {@link TypeTemplate}
     * @see SchemaBuilder#registerEntity(String, BiFunction)
     * @see SchemaBuilder#registerEntity(String, Supplier)
     * @see SchemaBuilder#registerEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerEntitySimple(String)
     * @see SchemaBuilder#registerEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    void registerEntity(String id, TypeTemplateFunction template);

    /**
     * Registers a new block entity definition
     *
     * <p>
     * Example:
     * <pre>{@code
     * SchemaBuilder builder = ...
     * // registers an entity definition that just passes data through via DSL::remainder
     * // equivalent to registerSimple
     * builder.registerEntity(
     *      "foo:bar",
     *      (name, schema) -> DSL::remainder
     * );
     * // registers an entity definition that specifies an optional item stack field
     * builder.registerEntity(
     *      "foo:bar",
     *      (name, schema) -> DSL.optionalFields("TestItem", References.ITEM_STACK.in(schema))
     * );
     * }</pre>
     *
     * @param id       the id of the block entity, in {@link Identifier}'s string format
     * @param template a functional interface that takes a {@link Schema} and returns a {@link TypeTemplate}
     * @see SchemaBuilder#registerBlockEntity(String, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(String, Supplier)
     * @see SchemaBuilder#registerBlockEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerBlockEntitySimple(String)
     * @see SchemaBuilder#registerBlockEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    void registerBlockEntity(String id, TypeTemplateFunction template);

    Stream<DataFix> buildFixers(Schema schema);

    Map<String, TypeTemplateFunction> buildEntities();

    Map<String, TypeTemplateFunction> buildBlockEntities();

    /**
     * Registers a new entity definition
     *
     * @param id       the id of the entity, in {@link Identifier}'s string format
     * @param template a function that takes the id as a string and a {@link Schema}, and returns a {@link TypeTemplate}
     * @see SchemaBuilder#registerEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(String, Supplier)
     * @see SchemaBuilder#registerEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerEntitySimple(String)
     * @see SchemaBuilder#registerEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerEntity(String id, BiFunction<String, Schema, TypeTemplate> template) {
        this.registerEntity(id, (schema) -> template.apply(id, schema));
    }

    /**
     * Registers a new entity definition
     *
     * @param id       the id of the entity, in {@link Identifier}'s string format
     * @param template a supplier for the type template of the entity
     * @see SchemaBuilder#registerEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(String, BiFunction)
     * @see SchemaBuilder#registerEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerEntitySimple(String)
     * @see SchemaBuilder#registerEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerEntity(String id, Supplier<TypeTemplate> template) {
        this.registerEntity(id, (schema) -> template.get());
    }

    /**
     * Registers a new entity definition
     *
     * @param id       the id of the entity
     * @param template a supplier for the type template of the entity
     * @see SchemaBuilder#registerEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(String, BiFunction)
     * @see SchemaBuilder#registerEntity(String, Supplier)
     * @see SchemaBuilder#registerEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerEntitySimple(String)
     * @see SchemaBuilder#registerEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerEntity(Identifier id, TypeTemplateFunction template) {
        this.registerEntity(id.toString(), template);
    }

    /**
     * Registers a new entity definition
     *
     * @param id       the id of the entity
     * @param template a function that takes the id as a string and a {@link Schema}, and returns a {@link TypeTemplate}
     * @see SchemaBuilder#registerEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(String, BiFunction)
     * @see SchemaBuilder#registerEntity(String, Supplier)
     * @see SchemaBuilder#registerEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerEntitySimple(String)
     * @see SchemaBuilder#registerEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerEntity(Identifier id, BiFunction<String, Schema, TypeTemplate> template) {
        this.registerEntity(id.toString(), template);
    }

    /**
     * Registers a new entity definition
     *
     * @param id       the id of the entity
     * @param template a supplier for the type template of the entity
     * @see SchemaBuilder#registerEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(String, BiFunction)
     * @see SchemaBuilder#registerEntity(String, Supplier)
     * @see SchemaBuilder#registerEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerEntitySimple(String)
     * @see SchemaBuilder#registerEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerEntity(Identifier id, Supplier<TypeTemplate> template) {
        this.registerEntity(id.toString(), template);
    }

    /**
     * Registers a new entity definition
     *
     * @param id the id of the entity, in {@link Identifier}'s string format
     * @see SchemaBuilder#registerEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(String, BiFunction)
     * @see SchemaBuilder#registerEntity(String, Supplier)
     * @see SchemaBuilder#registerEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerEntitySimple(String id) {
        this.registerEntity(id, DSL::remainder);
    }

    /**
     * Registers a new entity definition
     *
     * @param id the id of the entity
     * @see SchemaBuilder#registerEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(String, BiFunction)
     * @see SchemaBuilder#registerEntity(String, Supplier)
     * @see SchemaBuilder#registerEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerEntitySimple(String)
     * @see DSL
     * @since 2.0.0
     */
    default void registerEntitySimple(Identifier id) {
        this.registerEntitySimple(id.toString());
    }

    /**
     * Registers a new block entity definition
     *
     * @param id       the id of the block entity, in {@link Identifier}'s string format
     * @param template a function that takes the id as a string and a {@link Schema}, and returns a {@link TypeTemplate}
     * @see SchemaBuilder#registerBlockEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(String, Supplier)
     * @see SchemaBuilder#registerBlockEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerBlockEntitySimple(String)
     * @see SchemaBuilder#registerBlockEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerBlockEntity(String id, BiFunction<String, Schema, TypeTemplate> template) {
        this.registerBlockEntity(id, (schema) -> template.apply(id, schema));
    }

    /**
     * Registers a new block entity definition
     *
     * @param id       the id of the block entity, in {@link Identifier}'s string format
     * @param template a supplier for the type template of the block entity
     * @see SchemaBuilder#registerBlockEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(String, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerBlockEntitySimple(String)
     * @see SchemaBuilder#registerBlockEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerBlockEntity(String id, Supplier<TypeTemplate> template) {
        this.registerBlockEntity(id, (schema) -> template.get());
    }

    /**
     * Registers a new block entity definition
     *
     * @param id       the id of the block entity
     * @param template a supplier for the type template of the block entity
     * @see SchemaBuilder#registerBlockEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(String, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(String, Supplier)
     * @see SchemaBuilder#registerBlockEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerBlockEntitySimple(String)
     * @see SchemaBuilder#registerBlockEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerBlockEntity(Identifier id, TypeTemplateFunction template) {
        this.registerBlockEntity(id.toString(), template);
    }

    /**
     * Registers a new block entity definition
     *
     * @param id       the id of the block entity
     * @param template a function that takes the id as a string and a {@link Schema}, and returns a {@link TypeTemplate}
     * @see SchemaBuilder#registerBlockEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(String, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(String, Supplier)
     * @see SchemaBuilder#registerBlockEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerBlockEntitySimple(String)
     * @see SchemaBuilder#registerBlockEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerBlockEntity(Identifier id, BiFunction<String, Schema, TypeTemplate> template) {
        this.registerBlockEntity(id.toString(), template);
    }

    /**
     * Registers a new block entity definition
     *
     * @param id       the id of the block entity
     * @param template a supplier for the type template of the block entity
     * @see SchemaBuilder#registerBlockEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(String, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(String, Supplier)
     * @see SchemaBuilder#registerBlockEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerBlockEntitySimple(String)
     * @see SchemaBuilder#registerBlockEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerBlockEntity(Identifier id, Supplier<TypeTemplate> template) {
        this.registerBlockEntity(id.toString(), template);
    }

    /**
     * Registers a new block entity definition
     *
     * @param id the id of the block entity
     * @see SchemaBuilder#registerBlockEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(String, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(String, Supplier)
     * @see SchemaBuilder#registerBlockEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerBlockEntitySimple(Identifier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerBlockEntitySimple(String id) {
        this.registerBlockEntity(id, DSL::remainder);
    }

    /**
     * Registers a new block entity definition
     *
     * @param id the id of the block entity
     * @see SchemaBuilder#registerBlockEntity(String, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(String, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(String, Supplier)
     * @see SchemaBuilder#registerBlockEntity(Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, BiFunction)
     * @see SchemaBuilder#registerBlockEntity(Identifier, Supplier)
     * @see SchemaBuilder#registerBlockEntitySimple(String)
     * @see DSL
     * @since 2.0.0
     */
    default void registerBlockEntitySimple(Identifier id) {
        this.registerBlockEntitySimple(id.toString());
    }

    @FunctionalInterface
    interface TypeTemplateFunction {
        TypeTemplate accept(Schema schema);
    }
}
