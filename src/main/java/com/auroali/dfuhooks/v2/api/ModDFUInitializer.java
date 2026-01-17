package com.auroali.dfuhooks.v2.api;

import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;

/**
 * DFU Hooks mod entrypoint, called during DFU setup
 *
 * @author Auroali
 * @since 2.0.0
 */
public abstract class ModDFUInitializer {
    /**
     * Used to set up {@link SchemaBuilder}s
     *
     * @param provider the provider used to create new {@link SchemaBuilder}s
     * @since 2.0.0
     */
    public abstract void init(SchemaBuilderProvider provider);

    /**
     * Called when {@link ItemStackComponentizationFix} is run. Can be overriden
     * to provide additional item componentization fixes
     *
     * @param itemStackData the stack data, such as the items id and count
     * @param dynamic       the original stack's nbt data
     * @since 2.0.0
     */
    public void fixItemComponents(ItemStackComponentizationFix.ItemStackData itemStackData, Dynamic<?> dynamic) {

    }
}
