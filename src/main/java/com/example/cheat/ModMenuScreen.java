package com.example.cheat;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ModMenuScreen extends Screen {

    private enum Category { VISUAL, MOVEMENT, COMBAT }
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
            rows.add(new Row("esp", "ESP", true, false));
            rows.add(new Row("hitbox", "Hitbox", true, true)); // Ayarlanabilir
            rows.add(new Row("xray", "X-Ray", true, false));
        } else if (cat == Category.MOVEMENT) {
            rows.add(new Row("fly", "Fly", true, true));
            rows.add(new Row("speed", "Speed", true, true));
            rows.add(new Row("speedmine", "SpeedMine", true, false));
        } else { // COMBAT
            rows.add(new Row("killaura", "Killaura", true, true));
        }
        return rows;
    }

    @Override
    protected void init() {
        int panelX = this.width / 2 - PANEL_WIDTH / 2;
        int panelY = this.height / 2 - PANEL_HEIGHT / 2;

        addSidebarButton(panelX, panelY, "G", Category.VISUAL);
        addSidebarButton(panelX, panelY + 46, "H", Category.MOVEMENT);
        addSidebarButton(panelX, panelY + 92, "C", Category.COMBAT);

        int listX = panelX + SIDEBAR_WIDTH;
        int listWidth = PANEL_WIDTH - SIDEBAR_WIDTH;
        int rowY = panelY + 40;

        for (Row row : rowsFor(currentCategory)) {
            addRow(listX, rowY, listWidth, row);
            rowY += ROW_HEIGHT + 6;

            if (row.hasDetail() && row.id().equals(expandedRow)) {
                if (row.id().equals("hitbox")) {
                    addHitboxSlider(listX + 10, rowY, listWidth - 20);
                } else if (row.id().equals("speed")) {
                    addSpeedSlider(listX + 10, rowY, listWidth - 20);
                } else if (row.id().equals("killaura")) {
                    // Killaura range slider eklenebilir
                }
                rowY += ROW_HEIGHT + 6;
            }
        }
    }

    // ... (mevcut addSidebarButton, addRow, getToggleState, toggleRow, addFlySpeedSlider methodlarını koru)

    private void addHitboxSlider(int x, int y, int width) {
        double normalized = CheatModClient.hitboxScale / 2.0;
        this.addDrawableChild(new SliderWidget(x, y, width, 18, Text.literal("Hitbox Scale: " + String.format("%.1f", CheatModClient.hitboxScale)), normalized) {
            @Override
            protected void updateMessage() {
                CheatModClient.hitboxScale = value * 2.0;
                setMessage(Text.literal("Hitbox Scale: " + String.format("%.1f", CheatModClient.hitboxScale)));
            }
            @Override
            protected void applyValue() {}
        });
    }

    private void addSpeedSlider(int x, int y, int width) {
        double normalized = (CheatModClient.speedMultiplier - 1.0) / 2.0;
        this.addDrawableChild(new SliderWidget(x, y, width, 18, Text.literal("Speed: " + String.format("%.1f", CheatModClient.speedMultiplier)), normalized) {
            @Override
            protected void updateMessage() {
                CheatModClient.speedMultiplier = 1.0 + value * 2.0;
                setMessage(Text.literal("Speed: " + String.format("%.1f", CheatModClient.speedMultiplier)));
            }
            @Override
            protected void applyValue() {}
        });
    }

    // toggleRow metoduna yeni case'ler ekle:
    // case "esp" -> CheatModClient.esp = !CheatModClient.esp;
    // case "hitbox" -> CheatModClient.hitbox = !CheatModClient.hitbox;
    // case "speed" -> CheatModClient.speed = !CheatModClient.speed;
    // case "speedmine" -> CheatModClient.speedmine = !CheatModClient.speedmine;
    // case "xray" -> CheatModClient.xray = !CheatModClient.xray;
    // case "killaura" -> CheatModClient.killaura = !CheatModClient.killaura;

    // getToggleState'e de ekle
}
