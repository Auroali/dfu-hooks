package com.auroali.dfuhooks.v2.mixin;

import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemStackComponentizationFix.ItemStackData.class)
public interface ItemStackComponentizationFix$ItemStackDataAccessor {
    @Accessor("item")
    String dfuhooks$item();
}
