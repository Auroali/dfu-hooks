package com.auroali.dfuhooks.test;

import com.auroali.dfuhooks.DFUHooksItemComponentHook;
import com.mojang.serialization.Dynamic;
import net.minecraft.datafixer.fix.ItemStackComponentizationFix;

public class ItemComponentizationTest extends DFUHooksItemComponentHook {
    @Override
    public void runHook(ItemStackComponentizationFix.StackData data, Dynamic<?> dynamic, int displayFlags) {
        System.out.println("Ran Item Componentization Fix");
    }
}
