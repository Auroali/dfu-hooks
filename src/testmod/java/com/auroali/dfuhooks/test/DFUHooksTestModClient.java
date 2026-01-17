package com.auroali.dfuhooks.test;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ZombieRenderer;

public class DFUHooksTestModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderers.register(DFUHooksTestMod.TEST_ENTITY, ZombieRenderer::new);
    }
}
