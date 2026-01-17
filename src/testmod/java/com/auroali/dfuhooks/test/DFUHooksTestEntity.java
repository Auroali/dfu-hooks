package com.auroali.dfuhooks.test;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class DFUHooksTestEntity extends Zombie {
    private ItemStack additionalItem;

    public DFUHooksTestEntity(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
        this.additionalItem = ItemStack.EMPTY;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.additionalItem = valueInput.read("additional_item", ItemStack.CODEC)
          .orElse(ItemStack.EMPTY);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        if (!this.additionalItem.isEmpty())
            valueOutput.store("additional_item", ItemStack.CODEC, this.additionalItem);
    }
}
