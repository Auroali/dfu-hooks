package com.auroali.dfuhooks.v2.mixin;

import com.auroali.dfuhooks.v2.api.ModDFUInitializer;
import com.auroali.dfuhooks.v2.impl.DFUHooks;
import com.mojang.serialization.Dynamic;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.datafix.fixes.ItemStackComponentizationFix;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ApiStatus.Internal
@Mixin(ItemStackComponentizationFix.class)
public class ItemStackComponentizationFixMixin {
    @Inject(method = "fixItemStack", at = @At("RETURN"))
    private static void dfuhooks$runCustomFixes(ItemStackComponentizationFix.ItemStackData itemStackData, Dynamic<?> dynamic, CallbackInfo ci) {
        try {
            FabricLoader.getInstance().invokeEntrypoints(
              "dfuhooks-init",
              ModDFUInitializer.class,
              init -> init.fixItemComponents(itemStackData, dynamic)
            );
        } catch (Throwable e) {
            DFUHooks.LOGGER.error(
              "Could not run custom item componentization fixes for {}",
              ((ItemStackComponentizationFix$ItemStackDataAccessor) itemStackData).dfuhooks$item(),
              e
            );
        }
    }
}
