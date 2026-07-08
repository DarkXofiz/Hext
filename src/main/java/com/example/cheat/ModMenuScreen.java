package com.example.cheat;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModMenuScreen extends Screen {

    private enum Category { VISUAL, MOVEMENT, MISC }
    private Category currentCategory = Category.VISUAL;

    private static final int SIDEBAR_WIDTH = 90;
    private static final int PANEL_WIDTH = 260;
    private static final int PANEL_HEIGHT = 200;

    private static final int COLOR_BG = 0xE6121216;
    private static final int COLOR_SIDEBAR = 0xE61A1A20;
    private static final int COLOR_ACCENT = 0xFF7B5CFA;
    private static final int COLOR_ACCENT_DIM = 0xFF3A2E66;
    private static final int COLOR_BORDER = 0xFF2A2A32;

    public ModMenuScreen() {
        super(Text.literal("Hext"));
    }

    @Override
    protected void init() {
        int panelX = this.width / 2 - PANEL_WIDTH / 2;
        int panelY = this.height / 2 - PANEL_HEIGHT / 2;

        int catY = panelY + 10;
        int catSpacing = 26;

        addCategoryButton(panelX + 6, catY, "Görsel", Category.VISUAL);
        addCategoryButton(panelX + 6, catY + catSpacing, "Hareket", Category.MOVEMENT);
        addCategoryButton(panelX + 6, catY + catSpacing * 2, "Diğer", Category.MISC);

        int contentX = panelX + SIDEBAR_WIDTH + 14;
        int contentY = panelY + 14;

        switch (currentCategory) {
            case VISUAL -> {
                addToggleRow(contentX, contentY, "Coordinate HUD",
                        () -> HudState.showCoords, v -> HudState.showCoords = v);
                addToggleRow(contentX, contentY + 26, "Zoom",
                        () -> HudState.zoomEnabled, v -> HudState.zoomEnabled = v);
            }
            case MOVEMENT -> addToggleRow(contentX, contentY, "Fly (Creative-like)",
                    () -> HudState.flyEnabled, v -> {
                        HudState.flyEnabled = v;
                        if (this.client != null && this.client.player != null) {
                            this.client.player.getAbilities().flying = v;
                            this.client.player.getAbilities().allowFlying = v;
                            this.client.player.sendAbilitiesUpdate();
                        }
                    });
            case MISC -> {
                // Buraya ileride yeni adil özellikler eklenebilir
            }
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Kapat"),
                b -> this.close())
                .dimensions(panelX + PANEL_WIDTH - 70, panelY + PANEL_HEIGHT - 26, 64, 18)
                .build());
    }

    private void addCategoryButton(int x, int y, String label, Category cat) {
        boolean active = currentCategory == cat;
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal((active ? "§d§l" : "§7") + label),
                b -> { currentCategory = cat; rebuild(); })
                .dimensions(x, y, SIDEBAR_WIDTH - 12, 20)
                .build());
    }

    private void addToggleRow(int x, int y, String label, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        boolean on = getter.get();
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal(label + "  " + (on ? "§d●" : "§8○")),
                button -> {
                    boolean newVal = !getter.get();
                    setter.accept(newVal);
                    button.setMessage(Text.literal(label + "  " + (newVal ? "§d●" : "§8○")));
                })
                .dimensions(x, y, PANEL_WIDTH - SIDEBAR_WIDTH - 28, 20)
                .build());
    }

    private void rebuild() {
        this.clearChildren();
        this.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        int panelX = this.width / 2 - PANEL_WIDTH / 2;
        int panelY = this.height / 2 - PANEL_HEIGHT / 2;

        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, COLOR_BG);
        context.drawBorder(panelX, panelY, PANEL_WIDTH, PANEL_HEIGHT, COLOR_BORDER);

        context.fill(panelX, panelY, panelX + SIDEBAR_WIDTH, panelY + PANEL_HEIGHT, COLOR_SIDEBAR);
        context.fill(panelX + SIDEBAR_WIDTH - 1, panelY, panelX + SIDEBAR_WIDTH, panelY + PANEL_HEIGHT, COLOR_ACCENT_DIM);

        context.drawTextWithShadow(this.textRenderer, "§d§lHEXT", panelX + 10, panelY - 12, 0xFFFFFF);

        int catIndex = currentCategory.ordinal();
        int highlightY = panelY + 10 + catIndex * 26;
        context.fill(panelX, highlightY - 1, panelX + 3, highlightY + 19, COLOR_ACCENT);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
