package com.hext.gui;

import com.hext.HextClient;
import com.hext.modules.BaseModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class HextScreen extends Screen {

    private static final int PANEL_COLOR       = 0xFF1A1A2E;
    private static final int PANEL_BORDER      = 0xFF7B2FBE;
    private static final int HEADER_COLOR      = 0xFF16213E;
    private static final int MODULE_BG         = 0xFF0F3460;
    private static final int MODULE_BG_HOVER   = 0xFF1A4A7A;
    private static final int MODULE_ENABLED    = 0xFF7B2FBE;
    private static final int MODULE_DISABLED   = 0xFF2D2D2D;
    private static final int SETTINGS_BG       = 0xFF1A1A2E;
    private static final int TEXT_WHITE        = 0xFFFFFFFF;
    private static final int TEXT_GRAY         = 0xFFAAAAAA;
    private static final int TEXT_PURPLE       = 0xFFBB86FC;
    private static final int ACCENT            = 0xFF7B2FBE;
    private static final int SHADOW            = 0xCC000000;

    private static final int PANEL_W     = 240;
    private static final int SETTINGS_W  = 200;
    private static final int PANEL_H_BASE = 50;
    private static final int MODULE_H    = 26;
    private static final int SETTING_H   = 30;
    private static final int PADDING     = 10;

    private int panelX, panelY;
    private int settingsX, settingsY;
    private boolean dragging = false;
    private int dragOffX, dragOffY;
    private int hoveredIndex = -1;
    private BaseModule selectedModule = null;

    public HextScreen() {
        super(Text.literal("Hext"));
    }

    @Override
    protected void init() {
        int totalW = PANEL_W + SETTINGS_W + PADDING * 2;
        int startX = (width - totalW) / 2;
        panelX = startX;
        settingsX = panelX + PANEL_W + PADDING;
        int totalH = Math.max(getPanelH(), 300);
        panelY = (height - totalH) / 2;
        settingsY = panelY;
    }

    private int getPanelH() {
        return PANEL_H_BASE + HextClient.modules.size() * MODULE_H + PADDING;
    }

    private int getSettingsH() {
        if (selectedModule == null) return 100;
        return 60 + selectedModule.getSettingCount() * SETTING_H;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, width, height, 0xBB000000);

        int ph = getPanelH();

        // === MODÜL PANELİ ===
        ctx.fill(panelX + 4, panelY + 4, panelX + PANEL_W + 4, panelY + ph + 4, SHADOW);
        ctx.fill(panelX - 2, panelY - 2, panelX + PANEL_W + 2, panelY + ph + 2, PANEL_BORDER);
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + ph, PANEL_COLOR);
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + 4, 0xFF2A2A4A);

        // Header
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + 36, HEADER_COLOR);
        ctx.fill(panelX, panelY + 36, panelX + PANEL_W, panelY + 37, ACCENT);
        ctx.drawText(textRenderer, "⚡ HEXT", panelX + PADDING, panelY + 12, TEXT_PURPLE, true);

        String ver = "v1.0";
        ctx.drawText(textRenderer, ver, panelX + PANEL_W - PADDING - textRenderer.getWidth(ver), panelY + 12, TEXT_GRAY, false);

        long active = HextClient.modules.stream().filter(m -> m.enabled).count();
        ctx.drawText(textRenderer, active + "/" + HextClient.modules.size() + " aktif", panelX + PADDING, panelY + 24, TEXT_GRAY, false);

        // Modül listesi
        List<BaseModule> modules = HextClient.modules;
        hoveredIndex = -1;

        for (int i = 0; i < modules.size(); i++) {
            BaseModule m = modules.get(i);
            int my = panelY + 42 + i * MODULE_H;
            int mx = panelX + PADDING;
            int mw = PANEL_W - PADDING * 2;

            boolean hover = mouseX >= mx && mouseX <= mx + mw
                    && mouseY >= my && mouseY <= my + MODULE_H - 2;
            if (hover) hoveredIndex = i;

            int rowBg = MODULE_BG;
            if (selectedModule == m) rowBg = 0xFF2A1A4A;
            else if (hover) rowBg = MODULE_BG_HOVER;
            ctx.fill(mx, my, mx + mw, my + MODULE_H - 2, rowBg);
            ctx.fill(mx, my, mx + 3, my + MODULE_H - 2, m.enabled ? MODULE_ENABLED : MODULE_DISABLED);

            int nameColor = m.enabled ? TEXT_PURPLE : TEXT_GRAY;
            if (selectedModule == m) nameColor = 0xFFFF6B6B;
            ctx.drawText(textRenderer, m.name, mx + 10, my + 8, nameColor, true);

            if (m.keyBinding != null && m.keyBinding.getDefaultKey().getCode() != -1) {
                String keyText = m.keyBinding.getBoundKeyLocalizedText().getString();
                if (!keyText.isEmpty() && !keyText.equals("Unknown")) {
                    ctx.drawText(textRenderer, keyText, mx + mw - 50, my + 8, TEXT_GRAY, false);
                }
            }

            int btnX = mx + mw - 30;
            int btnY = my + 6;
            int btnW = 24;
            int btnH = 12;

            if (m.enabled) {
                ctx.fill(btnX, btnY, btnX + btnW, btnY + btnH, MODULE_ENABLED);
                ctx.fill(btnX + btnW - 8, btnY + 1, btnX + btnW - 1, btnY + btnH - 1, TEXT_WHITE);
                ctx.drawText(textRenderer, "ON", btnX + 2, btnY + 2, TEXT_WHITE, false);
            } else {
                ctx.fill(btnX, btnY, btnX + btnW, btnY + btnH, 0xFF333333);
                ctx.fill(btnX + 1, btnY + 1, btnX + 9, btnY + btnH - 1, 0xFF666666);
                ctx.drawText(textRenderer, "OFF", btnX + 2, btnY + 2, TEXT_GRAY, false);
            }
        }

        ctx.drawText(textRenderer, "[ESC] Kapat  [Sürükle] Taşı", panelX + PADDING, panelY + ph - 14, TEXT_GRAY, false);

        // === AYAR PANELİ ===
        if (selectedModule != null) {
            int sh = getSettingsH();
            ctx.fill(settingsX + 4, settingsY + 4, settingsX + SETTINGS_W + 4, settingsY + sh + 4, SHADOW);
            ctx.fill(settingsX - 2, settingsY - 2, settingsX + SETTINGS_W + 2, settingsY + sh + 2, PANEL_BORDER);
            ctx.fill(settingsX, settingsY, settingsX + SETTINGS_W, settingsY + sh, SETTINGS_BG);
            ctx.fill(settingsX, settingsY, settingsX + SETTINGS_W, settingsY + 4, 0xFF2A2A4A);

            ctx.fill(settingsX, settingsY, settingsX + SETTINGS_W, settingsY + 32, HEADER_COLOR);
            ctx.fill(settingsX, settingsY + 32, settingsX + SETTINGS_W, settingsY + 33, ACCENT);
            ctx.drawText(textRenderer, "⚙ " + selectedModule.name, settingsX + PADDING, settingsY + 10, TEXT_PURPLE, true);
            ctx.drawText(textRenderer, "Ayar yok", settingsX + PADDING, settingsY + 45, TEXT_GRAY, false);
        }

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Header sürükle
        if (mouseX >= panelX && mouseX <= panelX + PANEL_W
                && mouseY >= panelY && mouseY <= panelY + 36) {
            dragging = true;
            dragOffX = (int) mouseX - panelX;
            dragOffY = (int) mouseY - panelY;
            return true;
        }

        // Modül toggle / seç
        List<BaseModule> modules = HextClient.modules;
        for (int i = 0; i < modules.size(); i++) {
            int my = panelY + 42 + i * MODULE_H;
            int mx = panelX + PADDING;
            int mw = PANEL_W - PADDING * 2;

            if (mouseX >= mx && mouseX <= mx + mw
                    && mouseY >= my && mouseY <= my + MODULE_H - 2) {

                int btnX = mx + mw - 30;
                int btnY = my + 6;
                int btnW = 24;
                int btnH = 12;

                if (mouseX >= btnX && mouseX <= btnX + btnW
                        && mouseY >= btnY && mouseY <= btnY + btnH) {
                    modules.get(i).toggle();
                    com.hext.config.Config.save();
                    return true;
                }

                selectedModule = (selectedModule == modules.get(i)) ? null : modules.get(i);
                return true;
            }
        }

        if (selectedModule != null
                && !(mouseX >= settingsX && mouseX <= settingsX + SETTINGS_W
                && mouseY >= settingsY && mouseY <= settingsY + getSettingsH())) {
            selectedModule = null;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            panelX = (int) mouseX - dragOffX;
            panelY = (int) mouseY - dragOffY;
            settingsX = panelX + PANEL_W + PADDING;
            settingsY = panelY;
            panelX = Math.max(0, Math.min(width - PANEL_W - SETTINGS_W - PADDING, panelX));
            panelY = Math.max(0, Math.min(height - getPanelH(), panelY));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
