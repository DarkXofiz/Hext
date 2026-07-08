package com.example.cheat;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ModMenuScreen extends Screen {

    private enum Category { VISUAL, MOVEMENT }
    private Category currentCategory = Category.VISUAL;

    private String expandedRow = null;

    private static final int SIDEBAR_WIDTH = 60;
    private static final int PANEL_WIDTH = 300;
    private static final int PANEL_HEIGHT = 260;
    private static final int ROW_HEIGHT = 26;

    private static final int COLOR_BG = 0xF0161622;
    private static final int COLOR_SIDEBAR = 0xF01B1B28;
    private static final int COLOR_ACCENT = 0xFF7B5CFA;
    private static final int COLOR_BORDER = 0xFF2C2C3A;

    public ModMenuScreen() {
        super(Text.literal("Hext"));
    }

    private record Row(String id, String label, boolean hasToggle, boolean hasDetail) {}

    private List<Row> rowsFor(Category cat) {
        List<Row> rows = new ArrayList<>();
        if (cat == Category.VISUAL) {
            rows.add(new Row("coords", "Coordinate HUD", true, false));
            rows.add(new Row("zoom", "Zoom", true, false));
        } else {
            rows.add(new Row("fly", "Fly", true, true));
        }
        return rows;
    }

    @Override
    protected void init() {
        int panelX = this.width / 2 - PANEL_WIDTH / 2;
        int panelY = this.height / 2 - PANEL_HEIGHT / 2;

        addSidebarButton(panelX, panelY, "G", Category.VISUAL);
        addSidebarButton(panelX, panelY + 46, "H", Category.MOVEMENT);

        int listX = panelX + SIDEBAR_WIDTH;
        int listWidth = PANEL_WIDTH - SIDEBAR_WIDTH;
        int rowY = panelY + 40;

        for (Row row : rowsFor(currentCategory)) {
            addRow(listX, rowY, listWidth, row);
            rowY += ROW_HEIGHT + 6;

            if (row.hasDetail() && row.id().equals(expandedRow)) {
                addFlySpeedSlider(listX + 10, rowY, listWidth - 20);
                rowY += ROW_HEIGHT + 6;
            }
        }
    }

    private void addSidebarButton(int x, int y, String icon, Category cat) {
        boolean active = currentCategory == cat;
        this.addDrawableChild(ButtonWidget.builder(
                Text.literal(active ? "§d§l" + icon : "§7" + icon),
                b -> { currentCategory = cat; expandedRow = null; rebuild(); })
                .dimensions(x + 8, y + 6, SIDEBAR_WIDTH - 16, 30)
                .build());
    }

    private void addRow(int x, int y, int width, Row row) {
        boolean on = getToggleState(row.id());
        String rightText = row.hasToggle() ? (on ? "§d●" : "§8○") : "›";

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal(row.label() + "   " + rightText),
                button -> {
                    if (row.hasDetail()) {
                        expandedRow = expandedRow != null && expandedRow.equals(row.id()) ? null : row.id();
                    }
                    toggleRow(row.id());
                    rebuild();
                })
                .dimensions(x, y, width, ROW_HEIGHT)
                .build());
    }

    private boolean getToggleState(String id) {
        return switch (id) {
            case "coords" -> HudState.showCoords;
            case "zoom" -> HudState.zoomEnabled;
            case "fly" -> HudState.flyEnabled;
            default -> false;
        };
    }

    private void toggleRow(String id) {
        switch (id) {
            case "coords" -> HudState.showCoords = !HudState.showCoords;
            case "zoom" -> HudState.zoomEnabled = !HudState.zoomEnabled;
            case "fly" -> {
                HudState.flyEnabled = !HudState.flyEnabled;
                if (this.client != null && this.client.player != null) {
                    this.client.player.getAbilities().flying = HudState.flyEnabled;
                    this.client.player.getAbilities().allowFlying = HudState.flyEnabled;
                    this.client.player.sendAbilitiesUpdate();
                }
            }
        }
    }

    private void addFlySpeedSlider(int x, int y, int width) {
        double normalized = (HudState.flySpeed - 0.05) / (1.0 - 0.05);
        this.addDrawableChild(new SliderWidget(x, y, width, 18,
                Text.literal("Hız: " + String.format("%.2f", HudState.flySpeed)), normalized) {
            @Override
            protected void updateMessage() {
                HudState.flySpeed = 0.05 + value * (1.0 - 0.05);
                setMessage(Text.literal("Hız: " + String.format("%.2f", HudState.flySpeed)));
            }

            @Override
            protected void applyValue() {
                if (client != null && client.player != null) {
                    client.player.getAbilities().setFlySpeed((float) HudState.flySpeed);
                    client.player.sendAbilitiesUpdate();
                }
            }
        });
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

        String title = currentCategory == Category.VISUAL ? "Görsel" : "Hareket";
        context.drawTextWithShadow(this.textRenderer, "§l" + title,
                panelX + SIDEBAR_WIDTH + 8, panelY + 10, 0xFFFFFF);

        context.drawTextWithShadow(this.textRenderer, "§d§lHEXT", panelX + 8, panelY - 12, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
                              }
