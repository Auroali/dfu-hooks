package com.auroali.dfuhooks.test;

import com.auroali.dfuhooks.DFUHooksItemComponentHook;
import com.mojang.serialization.Dynamic;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.datafixer.fix.ItemStackComponentizationFix;

public class ItemComponentizationTest extends DFUHooksItemComponentHook {
    @Override
    public void runHook(ItemStackComponentizationFix.StackData data, Dynamic<?> dynamic, int displayFlags) {
        // give all existing diamonds a max stack size of 16
        // this isn't a super great example, as this should be primarily used to move
        // existing nbt data to components (via functions like StackData#moveToComponent),
        // but there isn't really any extra data that can be moved for an example as minecraft
        // already handles all of that.
        //
        // its also worth noting that this runs before the modify schemas hook, so if you
        // add a fixer to change item ids you must use the old id here
        if(data.itemEquals("minecraft:diamond"))
            data.setComponent("minecraft:max_stack_size", dynamic.createInt(16));

        // usage of the moveToComponent method would be something like
        // data.moveToComponent("SomeData", "modid:some_component");
        // where the first argument is an nbt key, and the second is a component id.
        // this just moves whatever value is at the nbt key to the component specified by
        // the component id. more examples are in net.minecraft.datafixer.fix.ItemStackComponentizationFix (assuming yarn mappings)
    }
}
