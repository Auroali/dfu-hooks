package com.auroali.dfuhooks.test;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.zombie.Zombie;

public class DFUHooksTestMod implements ModInitializer {
    public static final ResourceKey<EntityType<?>> TEST_ENTITY_KEY = ResourceKey.create(
      Registries.ENTITY_TYPE,
      Identifier.fromNamespaceAndPath("dfuhooks-testmod", "test")
    );
    public static final EntityType<? extends Zombie> TEST_ENTITY = EntityType.Builder.of(
        DFUHooksTestEntity::new, MobCategory.MONSTER
      )
      .sized(0.6F, 1.95F)
      .eyeHeight(1.74F)
      .passengerAttachments(2.0125F)
      .ridingOffset(-0.7F)
      .clientTrackingRange(8)
      .notInPeaceful()
      .build(TEST_ENTITY_KEY);

    @Override
    public void onInitialize() {
        Registry.register(BuiltInRegistries.ENTITY_TYPE, TEST_ENTITY_KEY, TEST_ENTITY);
    }
}
