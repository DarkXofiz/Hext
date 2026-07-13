package com.hext.gui;

import com.hext.HextClient;
import com.hext.modules.BaseModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class HextScreen extends Screen {

    // Renk paleti - Neon koyu tema
    private static final int BG_COLOR         = 0xE5101010;
    private static final int PANEL_COLOR       = 0xFF1A1A2E;
    private static final int PANEL_BORDER      = 0xFF7B2FBE;
    private static final int HEADER_COLOR      = 0xFF16213E;
    private static final int MODULE_BG         = 0xFF0F3460;
    private static final int MODULE_BG_HOVER   = 0xFF1A4A7A;
    private static final int MODULE_ENABLED    = 0xFF7B2FBE;
    private static final int MODULE_DISABLED   = 0xFF2D2D2D;
    private static final int TEXT_WHITE        = 0xFFFFFFFF;
    private static final int TEXT_GRAY         = 0xFFAAAAAA;
    private static final int TEXT_PURPLE       = 0xFFBB86FC;
    private static final int ACCENT            = 0xFF7B2FBE;
    private static final int SHADOW            = 0xCC000000;

    private static final int PANEL_W   = 240;
    private static final int PANEL_H_BASE = 50;
    private static final int MODULE_H  = 26;
    private static final int PADDING   = 10;

    private int panelX, panelY;
    private boolean dragging = false;
    private int dragOffX, dragOffY;
    private int hoveredIndex = -1;
    private float scrollOffset = 0;
    private int maxScroll = 0;

    public HextScreen() {
        super(Text.literal("Hext"));
    }

    @Override
    protected void init() {
        panelX = (width - PANEL_W) / 2;
        panelY = (height - getPanelH()) / 2;
        // Ortala
        panelX = Math.max(0, Math.min(width - PANEL_W, panelX));
        panelY = Math.max(0, Math.min(height - getPanelH(), panelY));
    }

    private int getPanelH() {
        return PANEL_H_BASE + HextClient.modules.size() * MODULE_H + PADDING;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Arka plan blur efekti
        ctx.fill(0, 0, width, height, 0xBB000000);

        int ph = getPanelH();

        // Gölge
        ctx.fill(panelX + 4, panelY + 4, panelX + PANEL_W + 4, panelY + ph + 4, SHADOW);

        // Dış kenarlık (parlayan mor)
        ctx.fill(panelX - 2, panelY - 2, panelX + PANEL_W + 2, panelY + ph + 2, PANEL_BORDER);
        
        // Ana panel - gradient efekti için iki kat
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + ph, PANEL_COLOR);
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + 4, 0xFF2A2A4A);

        // Header
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + 36, HEADER_COLOR);
        ctx.fill(panelX, panelY + 36, panelX + PANEL_W, panelY + 37, ACCENT);

        // Logo
        String logo = "⚡ HEXT";
        ctx.drawText(textRenderer, logo, panelX + PADDING, panelY + 12, TEXT_PURPLE, true);

        // Versiyon
        String ver = "v1.0";
        ctx.drawText(textRenderer, ver, panelX + PANEL_W - PADDING - textRenderer.getWidth(ver), panelY + 12, TEXT_GRAY, false);

        // Modül sayacı
        long active = HextClient.modules.stream().filter(m -> m.enabled).count();
        String status = active + "/" + HextClient.modules.size() + " aktif";
        ctx.drawText(textRenderer, status, panelX + PADDING, panelY + 24, TEXT_GRAY, false);

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

            // Satır arkaplanı
            int rowBg = hover ? MODULE_BG_HOVER : MODULE_BG;
            ctx.fill(mx, my, mx + mw, my + MODULE_H - 2, rowBg);

            // Sol renkli şerit
            int stripColor = m.enabled ? MODULE_ENABLED : MODULE_DISABLED;
            ctx.fill(mx, my, mx + 3, my + MODULE_H - 2, stripColor);

            // Modül adı
            int nameColor = m.enabled ? TEXT_PURPLE : TEXT_GRAY;
            String displayName = m.name;
            ctx.drawText(textRenderer, displayName, mx + 10, my + 8, nameColor, true);

            // Keybinding gösterimi
            if (m.keyBinding != null && m.keyBinding.getDefaultKey().getCode() != -1) {
                String keyText = m.keyBinding.getBoundKeyLocalizedText().getString();
                if (!keyText.isEmpty() && !keyText.equals("Unknown")) {
                    ctx.drawText(textRenderer, keyText, mx + mw - 50, my + 8, TEXT_GRAY, false);
                }
            }

            // Toggle buton
            int btnX = mx + mw - 30;
            int btnY = my + 6;
            int btnW = 24;
            int btnH = 12;

            if (m.enabled) {
                ctx.fill(btnX, btnY, btnX + btnW, btnY + btnH, MODULE_ENABLED);
                ctx.fill(btnX + btnW - 8, btnY + 1, btnX + btnW - 1, btnY + btnH - 1, 0xFFFFFFFF);
                ctx.drawText(textRenderer, "ON", btnX + 2, btnY + 2, TEXT_WHITE, false);
            } else {
                ctx.fill(btnX, btnY, btnX + btnW, btnY + btnH, 0xFF333333);
                ctx.fill(btnX + 1, btnY + 1, btnX + 9, btnY + btnH - 1, 0xFF666666);
                ctx.drawText(textRenderer, "OFF", btnX + 2, btnY + 2, TEXT_GRAY, false);
            }
        }

        // Alt bilgi
        ctx.drawText(textRenderer, "[K] Kapat  [Sürükle] Taşı", panelX + PADDING, panelY + ph - 14, TEXT_GRAY, false);

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int ph = getPanelH();

        // Header'a tıkla - sürükle
        if (mouseX >= panelX && mouseX <= panelX + PANEL_W
                && mouseY >= panelY && mouseY <= panelY + 36) {
            dragging = true;
            dragOffX = (int) mouseX - panelX;
            dragOffY = (int) mouseY - panelY;
            return true;
        }

        // Modül toggle
        List<BaseModule> modules = HextClient.modules;
        for (int i = 0; i < modules.size(); i++) {
            int my = panelY + 42 + i * MODULE_H;
            int mx = panelX + PADDING;
            int mw = PANEL_W - PADDING * 2;

            if (mouseX >= mx && mouseX <= mx + mw
                    && mouseY >= my && mouseY <= my + MODULE_H - 2) {
                modules.get(i).toggle();
                com.hext.config.Config.save();
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            panelX = (int) mouseX - dragOffX;
            panelY = (int) mouseY - dragOffY;
            panelX = Math.max(0, Math.min(width - PANEL_W, panelX));
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // K tuşu ile menüyü kapat
        if (HextClient.openGuiKey.matchesKey(keyCode, scanCode)) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void close() {
        super.close();
    }
}
