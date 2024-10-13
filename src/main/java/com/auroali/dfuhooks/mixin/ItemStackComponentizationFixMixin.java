package com.auroali.dfuhooks.mixin;

import com.auroali.dfuhooks.DFUHooksItemComponentHook;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Dynamic;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.datafixer.fix.ItemStackComponentizationFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackComponentizationFix.class)
public class ItemStackComponentizationFixMixin {
	@Inject(method = "fixStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/datafixer/fix/ItemStackComponentizationFix$StackData;moveToComponent(Ljava/lang/String;Ljava/lang/String;)V", ordinal = 0))
	private static void dfuhooks$callItemStackComponentFix(ItemStackComponentizationFix.StackData data, Dynamic<?> dynamic, CallbackInfo ci, @Local(ordinal = 0) int flags) {
		FabricLoader.getInstance().invokeEntrypoints("dfuhooks-item-components", DFUHooksItemComponentHook.class, hook -> hook.runHook(data, dynamic, flags));
	}
}