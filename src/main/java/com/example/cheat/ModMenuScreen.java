package com.example.cheat;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ModMenuScreen extends Screen {

    private enum Tab { VISUAL, MOVEMENT, MISC }
    private Tab currentTab = Tab.VISUAL;

    private static final int PANEL_WIDTH = 220;
    private static final int PANEL_HEIGHT = 180;

    public ModMenuScreen() {
        super(Text.literal("Hext Menu"));
    }

    @Override
    protected void init() {
        int panelX = this.width / 2 - PANEL_WIDTH / 2;
        int panelY = this.height / 2 - PANEL_HEIGHT / 2;

        int tabWidth = PANEL_WIDTH / 3;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Görsel"),
                b -> { currentTab = Tab.VISUAL; rebuild(); })
                .dimensions(panelX, panelY, tabWidth, 18).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Hareket"),
                b -> { currentTab = Tab.MOVEMENT; rebuild(); })
                .dimensions(panelX + tabWidth, panelY, tabWidth, 18).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Diğer"),
                b -> { currentTab = Tab.MISC; rebuild(); })
                .dimensions(panelX + tabWidth * 2, panelY, tabWidth, 18).build());

        int contentY = panelY + 26;

        switch (currentTab) {
            case VISUAL -> {
                addToggleButton(panelX, contentY, "Coordinate HUD",
                        () -> HudState.showCoords,
                        v -> HudState.showCoords = v);
                addToggleButton(panelX, contentY + 24, "Zoom",
                        () -> HudState.zoomEnabled,
                        v -> HudState.zoomEnabled = v);
            }
            case MOVEMENT -> {
                addToggleButton(panelX, contentY, "Fly (Creative-like)",
                        () -> HudState.flyEnabled,
                        v -> {
                            HudState.flyEnabled = v;
                            if (this.client != null && this.client.player != null) {
                                this.client.player.getAbilities().flying = v;
                                this.client.player.getAbilities().allowFlying = v;
                                this.client.player.sendAbilitiesUpdate();
                            }
                        });
            }
            case MISC -> {
                // Buraya ileride yeni adil ayarlar eklenebilir
            }
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Kapat"),
                b -> this.close())
                .dimensions(panelX, panelY + PANEL_HEIGHT - 24, PANEL_WIDTH, 20)
                .build());
    }

    private void addToggleButton(int x, int y, String label, java.util.function.Supplier<Boolean> getter,
                                  java.util.function.Consumer<Boolean> setter) {
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal(label + ": " + (getter.get() ? "§aON" : "§cOFF")),
                button -> {
                    boolean newVal = !getter.get();
                    setter.accept(newVal);
                    button.setMessage(Text.literal(label + ": " + (newVal ? "§aON" : "§cOFF")));
                })
                .dimensions(x, y, PANEL_WIDTH, 20)
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

        context.fill(panelX - 4, panelY - 22, panelX + PANEL_WIDTH + 4, panelY + PANEL_HEIGHT + 4, 0xCC101015);
        context.drawBorder(panelX - 4, panelY - 22, PANEL_WIDTH + 8, PANEL_HEIGHT + 26, 0xFF3A9BFF);

        context.drawCenteredTextWithShadow(this.textRenderer, "§b§lHext Menu",
                this.width / 2, panelY - 16, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
