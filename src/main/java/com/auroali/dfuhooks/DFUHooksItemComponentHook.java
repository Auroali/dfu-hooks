package com.auroali.dfuhooks;

import com.mojang.serialization.Dynamic;
import net.minecraft.datafixer.fix.ItemStackComponentizationFix;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class DFUHooksItemComponentHook {
    public abstract void runHook(ItemStackComponentizationFix.StackData data, Dynamic<?> dynamic, int displayFlags);
    protected void fixAttributes(ItemStackComponentizationFix.StackData data, Dynamic<?> dynamic, int hideFlags) {
        ItemStackComponentizationFix.fixAttributeModifiers(data, dynamic, hideFlags);
    }
    protected void fixEnchantments(ItemStackComponentizationFix.StackData data, Dynamic<?> dynamic, String nbtKey, String componentId, boolean hideInTooltip) {
        ItemStackComponentizationFix.fixEnchantments(data, dynamic, nbtKey, componentId, hideInTooltip);
    }
    protected boolean itemEqualsIdentifier(ItemStackComponentizationFix.StackData data, Identifier id) {
        return data.itemEquals(id.toString());
    }
    protected boolean itemMatchesIdentifiers(ItemStackComponentizationFix.StackData data, Set<Identifier> identifiers) {
        return data.itemMatches(identifiers.stream().map(Identifier::toString).collect(Collectors.toSet()));
    }

}
