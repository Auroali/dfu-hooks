package com.auroali.dfuhooks.v2.api;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

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
     * Adds a new data fixer to the schema
     *
     * @param fix a function taking a {@link Schema} instance and returning a {@link DataFix}
     * @since 2.0.0
     */
    void addDataFixer(Function<Schema, DataFix> fix);

    /**
     * Registers a new type template definition for the given id and target
     * <p>
     * Example:
     * <pre>{@code
     * SchemaBuilder builder = ...
     * // registers an entity definition that just passes data through via DSL::remainder
     * // equivalent to registerSimple
     * builder.registerForTarget(
     *      Target.ENTITY,
     *      "foo:bar",
     *      (name, schema) -> DSL::remainder
     * );
     * // registers a block entity definition that specifies an optional item stack field
     * builder.registerForTarget(
     *      Target.BLOCK_ENTITY,
     *      "foo:bar",
     *      (name, schema) -> DSL.optionalFields("TestItem", References.ITEM_STACK.in(schema))
     * );
     * }</pre>
     *
     * @param target   the target, either an entity or block entity
     * @param id       the id of the entry for the given target
     * @param template the type template function
     * @see SchemaBuilder#registerForTarget(Target, Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerForTarget(Target, Identifier, BiFunction)
     * @see SchemaBuilder#registerForTarget(Target, Identifier, Supplier)
     * @see DSL
     * @since 2.0.0
     */
    void registerForTarget(Target target, String id, TypeTemplateFunction template);

    /**
     * Similar to {@link SchemaBuilder#registerForTarget(Target, String, TypeTemplateFunction)},
     * but the supplied type template is instead added to an existing type using {@link DSL#allWithRemainder(TypeTemplate, TypeTemplate...)}
     *
     * @param target   the target, either an entity or block entity
     * @param id       the id of the entry for the given target
     * @param template the type template function
     * @see DSL
     * @since 2.0.0
     */
    void registerExtension(Target target, String id, TypeTemplateFunction template);

    @ApiStatus.Internal
    Stream<DataFix> buildDataFixers(Schema schema);

    @ApiStatus.Internal
    Map<String, Supplier<TypeTemplate>> buildForTarget(Target target, Schema schema, Map<String, Supplier<TypeTemplate>> existingTypes);

    /**
     * Registers a new type template definition for the given id and target
     *
     * @param target   the target, either an entity or block entity
     * @param id       the id of the entry for the given target, in {@link Identifier}'s string format
     * @param template a function that takes the id as a string and a {@link Schema}, and returns a {@link TypeTemplate}
     * @see SchemaBuilder#registerForTarget(Target, Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerForTarget(Target, Identifier, BiFunction)
     * @see SchemaBuilder#registerForTarget(Target, Identifier, Supplier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerForTarget(Target target, String id, BiFunction<String, Schema, TypeTemplate> template) {
        this.registerForTarget(target, id, (schema) -> template.apply(id, schema));
    }

    /**
     * Registers a new type template definition for the given id and target
     *
     * @param target   the target, either an entity or block entity
     * @param id       the id of the entry for the given target, in {@link Identifier}'s string format
     * @param template a supplier for the type template of the entity
     * @see SchemaBuilder#registerForTarget(Target, Identifier, TypeTemplateFunction)
     * @see SchemaBuilder#registerForTarget(Target, Identifier, BiFunction)
     * @see SchemaBuilder#registerForTarget(Target, Identifier, Supplier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerForTarget(Target target, String id, Supplier<TypeTemplate> template) {
        this.registerForTarget(target, id, (schema) -> template.get());
    }

    /**
     * Registers a new type template definition for the given id and target
     *
     * @param target   the target, either an entity or block entity
     * @param id       the id of the entry for the given target
     * @param template a supplier for the type template of the entity
     * @see SchemaBuilder#registerForTarget(Target, String, TypeTemplateFunction)
     * @see SchemaBuilder#registerForTarget(Target, String, BiFunction)
     * @see SchemaBuilder#registerForTarget(Target, String, Supplier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerForTarget(Target target, Identifier id, TypeTemplateFunction template) {
        this.registerForTarget(target, id.toString(), template);
    }

    /**
     * Registers a new type template definition for the given id and target
     *
     * @param target   the target, either an entity or block entity
     * @param id       the id of the entry for the given target
     * @param template a function that takes the id as a string and a {@link Schema}, and returns a {@link TypeTemplate}
     * @see SchemaBuilder#registerForTarget(Target, String, TypeTemplateFunction)
     * @see SchemaBuilder#registerForTarget(Target, String, BiFunction)
     * @see SchemaBuilder#registerForTarget(Target, String, Supplier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerForTarget(Target target, Identifier id, BiFunction<String, Schema, TypeTemplate> template) {
        this.registerForTarget(target, id.toString(), template);
    }

    /**
     * Registers a new type template definition for the given id and target
     *
     * @param target   the target, either an entity or block entity
     * @param id       the id of the entry for the given target
     * @param template a supplier for the type template of the entity
     * @see SchemaBuilder#registerForTarget(Target, String, TypeTemplateFunction)
     * @see SchemaBuilder#registerForTarget(Target, String, BiFunction)
     * @see SchemaBuilder#registerForTarget(Target, String, Supplier)
     * @see DSL
     * @since 2.0.0
     */
    default void registerForTarget(Target target, Identifier id, Supplier<TypeTemplate> template) {
        this.registerForTarget(target, id.toString(), template);
    }

    /**
     * Registers a new type template definition for the given id and target
     *
     * @param target the target, either an entity or block entity
     * @param id     the id of the entry for the given target, in {@link Identifier}'s string format
     * @see SchemaBuilder#registerForTargetSimple(Target, Identifier)
     * @since 2.0.0
     */
    default void registerForTargetSimple(Target target, String id) {
        this.registerForTarget(target, id, DSL::remainder);
    }

    /**
     * Registers a new type template definition for the given id and target
     *
     * @param target the target, either an entity or block entity
     * @param id     the id of the entry for the given target
     * @see SchemaBuilder#registerForTargetSimple(Target, String)
     * @since 2.0.0
     */
    default void registerForTargetSimple(Target target, Identifier id) {
        this.registerForTargetSimple(target, id.toString());
    }

    @FunctionalInterface
    interface TypeTemplateFunction {
        TypeTemplate accept(Schema schema);
    }

    enum Target {
        ENTITY,
        BLOCK_ENTITY;

        public boolean checkId(String id) {
            return id.indexOf(Identifier.NAMESPACE_SEPARATOR) != -1 && Identifier.tryParse(id) != null;
        }

        public boolean canRegisterType() {
            return true;
        }

        public boolean canRegisterExtension() {
            return true;
        }
    }
}
