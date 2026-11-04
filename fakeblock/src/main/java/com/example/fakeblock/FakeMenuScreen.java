package com.example.fakeblock;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/** A purely cosmetic "Skeleton Spawner" menu. Buttons do nothing but close/flash text. */
public class FakeMenuScreen extends Screen {
    private static final int W = 176, H = 110;

    public FakeMenuScreen() {
        super(Text.literal(FakeBlockMod.SPAWNER_NAME));
    }

    @Override
    protected void init() {
        int left = (width - W) / 2, top = (height - H) / 2;
        addDrawableChild(ButtonWidget.builder(Text.literal("Storage"), b -> {}).dimensions(left + 13, top + 30, 70, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Drops"), b -> {}).dimensions(left + 93, top + 30, 70, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Sell All"), b -> {}).dimensions(left + 13, top + 55, 70, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close()).dimensions(left + 93, top + 55, 70, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        int left = (width - W) / 2, top = (height - H) / 2;
        // panel is drawn after the background but the buttons are added widgets, so draw panel first via fill under text
        ctx.drawCenteredTextWithShadow(textRenderer, title, width / 2, top + 10, 0xFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("Stack: 1  |  Skeleton"), width / 2, top + 85, 0xAAAAAA);
    }
}
