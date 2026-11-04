package com.example.fakeblock.mixin;

import com.example.fakeblock.FakeBlockMod;
import com.example.fakeblock.FakeMenuScreen;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientInteractMixin {
    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void fakeblock$openMenu(ClientPlayerEntity player, Hand hand, BlockHitResult hit,
                                    CallbackInfoReturnable<ActionResult> cir) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!FakeBlockMod.enabled || mc.world == null || player.isSneaking()) return; // shift = place blocks normally
        var pos = hit.getBlockPos();
        if (FakeBlockMod.FAKE.containsKey(pos) && mc.world.getBlockState(pos).isOf(Blocks.SPAWNER)) {
            if (hand == Hand.MAIN_HAND) {
                mc.setScreen(new FakeMenuScreen());
            }
            cir.setReturnValue(ActionResult.SUCCESS); // nothing is sent to the server
        }
    }
}
