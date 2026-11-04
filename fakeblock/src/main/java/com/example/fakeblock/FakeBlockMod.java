package com.example.fakeblock;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

/**
 * Client-side only. Press K (rebindable in Controls) to toggle.
 * While ON: black wool near you looks like a spawner, and right clicking it opens a fake menu.
 * While OFF: everything is restored.
 */
public class FakeBlockMod implements ClientModInitializer {
    // Edit this to change the menu title / name shown
    public static final String SPAWNER_NAME = "Skeleton Spawner";

    public static boolean enabled = false;
    public static final Map<BlockPos, BlockState> FAKE = new HashMap<>();

    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.fakeblock.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, "category.fakeblock"));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
                if (!enabled) restore(mc);
                if (mc.player != null) {
                    mc.player.sendMessage(Text.literal("Fake spawner: " + (enabled ? "ON" : "OFF")), true);
                }
            }
            if (enabled && mc.world != null && mc.player != null && mc.world.getTime() % 20 == 0) {
                scan(mc);
            }
        });
    }

    private static void scan(MinecraftClient mc) {
        BlockPos p = mc.player.getBlockPos();
        for (BlockPos pos : BlockPos.iterate(p.add(-32, -16, -32), p.add(32, 16, 32))) {
            BlockState state = mc.world.getBlockState(pos);
            if (state.isOf(Blocks.BLACK_WOOL)) {
                FAKE.put(pos.toImmutable(), state);
                mc.world.setBlockState(pos, Blocks.SPAWNER.getDefaultState());
            }
        }
    }

    private static void restore(MinecraftClient mc) {
        if (mc.world != null) {
            FAKE.forEach((pos, original) -> {
                if (mc.world.getBlockState(pos).isOf(Blocks.SPAWNER)) {
                    mc.world.setBlockState(pos, original);
                }
            });
        }
        FAKE.clear();
    }
}
