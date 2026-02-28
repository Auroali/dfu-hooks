package com.auroali.dfuhooks.v2.impl;

import com.auroali.dfuhooks.v2.api.SchemaBuilder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ApiStatus.Internal
public class DefaultSchemaBuilder implements SchemaBuilder {
    private final Map<Target, Map<String, TypeTemplateFunction>> templates;
    private final Map<Target, Multimap<String, TypeTemplateFunction>> extensions;
    private final List<Function<Schema, DataFix>> fixers;

    public DefaultSchemaBuilder() {
        this.templates = new HashMap<>();
        this.extensions = new HashMap<>();
        this.fixers = new ArrayList<>();
        for (Target target : Target.values()) {
            this.templates.put(target, new HashMap<>());
            this.extensions.put(target, HashMultimap.create());
        }
    }

    @Override
    public void addFixer(Function<Schema, DataFix> fix) {
        this.fixers.add(fix);
    }

    @Override
    public void registerForTarget(Target target, String id, TypeTemplateFunction template) {
        if (!target.canRegisterType()) {
            throw new IllegalArgumentException("target " + target + " does not support registering types");
        }
        Map<String, TypeTemplateFunction> targetTemplates = this.templates.get(target);
        if (targetTemplates.containsKey(id))
            throw new IllegalArgumentException(id + " is already registered for target " + target);
        if (!target.checkId(id))
            throw new IllegalArgumentException(id + " is not a valid id for target " + target);
        targetTemplates.put(id, template);
    }

    @Override
    public void registerExtension(Target target, String id, TypeTemplateFunction template) {
        if (!target.canRegisterExtension()) {
            throw new IllegalArgumentException("target " + target + " does not support registering extensions");
        }
        Multimap<String, TypeTemplateFunction> targetExtensions = this.extensions.get(target);
        if (!target.checkId(id))
            throw new IllegalArgumentException(id + " is not a valid id for target " + target);
        targetExtensions.put(id, template);
    }

    @Override
    public Stream<DataFix> buildFixers(Schema schema) {
        return this.fixers.stream().map(builder -> builder.apply(schema));
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> buildForTarget(Target target, Schema schema, Map<String, Supplier<TypeTemplate>> existingTypes) {
        if (target.canRegisterType()) {
            Map<String, TypeTemplateFunction> targetTemplates = this.templates.get(target);
            for (var template : targetTemplates.entrySet()) {
                existingTypes.put(template.getKey(), () -> template.getValue().accept(schema));
                DFUHooks.LOGGER.debug("Registered {} for {} in schema {}", template.getKey(), target, schema.getVersionKey());
            }
        }

        if (target.canRegisterExtension()) {
            Multimap<String, TypeTemplateFunction> targetExtensions = this.extensions.get(target);
            for (var extensionId : targetExtensions.keys()) {
                if (!existingTypes.containsKey(extensionId))
                    throw new IllegalArgumentException("Cannot extend '" + extensionId + "' for target " + target + " as it does not existing in the schema for version " + schema.getVersionKey());

                Collection<TypeTemplateFunction> templateFunctions = targetExtensions.get(extensionId);
                existingTypes.compute(extensionId, (_, existingTemplate) -> () ->
                  DSL.allWithRemainder(
                    existingTemplate.get(),
                    templateFunctions.stream()
                      .map(func -> func.accept(schema))
                      .toArray(TypeTemplate[]::new)
                  )
                );
                DFUHooks.LOGGER.debug("Extended type {} for {} in schema {}", extensionId, target, schema.getVersionKey());
            }
        }
        return existingTypes;
    }
}
