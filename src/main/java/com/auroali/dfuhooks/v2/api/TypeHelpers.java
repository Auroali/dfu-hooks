package com.auroali.dfuhooks.v2.api;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.V1458;

/**
 * Utility class that contains {@link TypeTemplate} generating methods
 *
 * @author Auroali
 * @since 2.0.0
 */
public class TypeHelpers {
    /**
     * Returns a {@link TypeTemplate} with an optional list of ItemStacks field
     * called Items, and an optional TextComponent field called CustomName
     *
     * <p>Wrapper around {@link V1458#nameableInventory(Schema)}</p>
     *
     * @param schema the in schema
     * @return the type template
     * @since 2.0.0
     */
    public static TypeTemplate nameableInventory(Schema schema) {
        return V1458.nameableInventory(schema);
    }

    /**
     * Returns a {@link TypeTemplate} with  an optional TextComponent field called CustomName
     *
     * <p>Wrapper around {@link V1458#nameable(Schema)}</p>
     *
     * @param schema the in schema
     * @return the type template
     * @since 2.0.0
     */
    public static TypeTemplate nameable(Schema schema) {
        return V1458.nameable(schema);
    }

    /**
     * Returns a {@link TypeTemplate} with an optional list of ItemStacks field
     * called Items
     *
     * @param schema the in schema
     * @return the type template
     * @since 2.0.0
     */
    public static TypeTemplate inventory(Schema schema) {
        return DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)));
    }
}
