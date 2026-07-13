package com.hext.gui;

import com.hext.HextClient;
import com.hext.modules.BaseModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class HextScreen extends Screen {

    // Renk paleti - Thunder tarzı koyu tema
    private static final int BG_COLOR         = 0xE5101010; // ana arka plan
    private static final int PANEL_COLOR       = 0xFF1A1A2E; // panel
    private static final int PANEL_BORDER      = 0xFF7B2FBE; // mor kenarlık
    private static final int HEADER_COLOR      = 0xFF16213E; // başlık arkaplan
    private static final int MODULE_BG         = 0xFF0F3460; // modül satırı
    private static final int MODULE_BG_HOVER   = 0xFF1A4A7A; // hover
    private static final int MODULE_ENABLED    = 0xFF7B2FBE; // aktif mor
    private static final int MODULE_DISABLED   = 0xFF2D2D2D; // pasif
    private static final int TEXT_WHITE        = 0xFFFFFFFF;
    private static final int TEXT_GRAY         = 0xFFAAAAAA;
    private static final int TEXT_PURPLE       = 0xFFBB86FC;
    private static final int ACCENT            = 0xFF7B2FBE;

    private static final int PANEL_W   = 220;
    private static final int PANEL_H_BASE = 50;
    private static final int MODULE_H  = 24;
    private static final int PADDING   = 8;

    // Sürükleme
    private int panelX = -1, panelY = -1;
    private boolean dragging = false;
    private int dragOffX, dragOffY;
    private int hoveredIndex = -1;

    public HextScreen() {
        super(Text.literal("Hext"));
    }

    @Override
    protected void init() {
        if (panelX == -1) {
            panelX = (width - PANEL_W) / 2;
            panelY = (height - getPanelH()) / 2;
        }
    }

    private int getPanelH() {
        return PANEL_H_BASE + HextClient.modules.size() * MODULE_H + PADDING;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // Yarı saydam arka plan
        ctx.fill(0, 0, width, height, 0x88000000);

        int ph = getPanelH();

        // Dış kenarlık (mor glow efekti)
        ctx.fill(panelX - 2, panelY - 2, panelX + PANEL_W + 2, panelY + ph + 2, PANEL_BORDER);
        // Ana panel
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + ph, PANEL_COLOR);

        // Header
        ctx.fill(panelX, panelY, panelX + PANEL_W, panelY + 32, HEADER_COLOR);
        // Header alt çizgi
        ctx.fill(panelX, panelY + 32, panelX + PANEL_W, panelY + 33, ACCENT);

        // Logo / başlık
        ctx.drawText(textRenderer, "⬡ HEXT", panelX + PADDING, panelY + 10, TEXT_PURPLE, true);
        // Versiyon
        ctx.drawText(textRenderer, "v1.0", panelX + PANEL_W - 28, panelY + 10, TEXT_GRAY, false);
        // Aktif modül sayısı
        long activeCount = HextClient.modules.stream().filter(m -> m.enabled).count();
        String statusText = activeCount + " aktif";
        ctx.drawText(textRenderer, statusText, panelX + PADDING, panelY + 20, TEXT_GRAY, false);

        // Modül listesi
        List<BaseModule> modules = HextClient.modules;
        hoveredIndex = -1;

        for (int i = 0; i < modules.size(); i++) {
            BaseModule m = modules.get(i);
            int my = panelY + 38 + i * MODULE_H;
            int mx = panelX + PADDING;
            int mw = PANEL_W - PADDING * 2;

            boolean hover = mouseX >= mx && mouseX <= mx + mw
                    && mouseY >= my && mouseY <= my + MODULE_H - 2;
            if (hover) hoveredIndex = i;

            // Satır arkaplanı
            int rowBg = hover ? MODULE_BG_HOVER : MODULE_BG;
            ctx.fill(mx, my, mx + mw, my + MODULE_H - 2, rowBg);

            // Sol renkli şerit (aktif = mor, pasif = gri)
            int stripColor = m.enabled ? MODULE_ENABLED : MODULE_DISABLED;
            ctx.fill(mx, my, mx + 3, my + MODULE_H - 2, stripColor);

            // Modül adı
            int nameColor = m.enabled ? TEXT_PURPLE : TEXT_GRAY;
            ctx.drawText(textRenderer, m.name, mx + 8, my + 7, nameColor, true);

            // Toggle buton (sağ taraf)
            int btnX = mx + mw - 28;
            int btnY = my + 5;
            int btnW = 24;
            int btnH = 12;

            if (m.enabled) {
                // ON - mor arka plan
                ctx.fill(btnX, btnY, btnX + btnW, btnY + btnH, MODULE_ENABLED);
                // Yuvarlak top sağa
                ctx.fill(btnX + btnW - 10, btnY + 1, btnX + btnW - 1, btnY + btnH - 1, 0xFFFFFFFF);
                ctx.drawText(textRenderer, "ON", btnX + 2, btnY + 2, TEXT_WHITE, false);
            } else {
                // OFF - koyu
                ctx.fill(btnX, btnY, btnX + btnW, btnY + btnH, 0xFF333333);
                ctx.fill(btnX + 1, btnY + 1, btnX + 10, btnY + btnH - 1, 0xFFAAAAAA);
                ctx.drawText(textRenderer, "OFF", btnX + 2, btnY + 2, TEXT_GRAY, false);
            }
        }

        // Alt bilgi
        ctx.drawText(textRenderer, "Kapat: ESC", panelX + PADDING, panelY + ph - 12, TEXT_GRAY, false);

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int ph = getPanelH();

        // Header'a tıklanırsa sürükle
        if (mouseX >= panelX && mouseX <= panelX + PANEL_W
                && mouseY >= panelY && mouseY <= panelY + 32) {
            dragging = true;
            dragOffX = (int) mouseX - panelX;
            dragOffY = (int) mouseY - panelY;
            return true;
        }

        // Modüle tıklanırsa toggle
        List<BaseModule> modules = HextClient.modules;
        for (int i = 0; i < modules.size(); i++) {
            int my = panelY + 38 + i * MODULE_H;
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
            // Ekran dışına çıkmasın
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
    public boolean shouldPause() {
        return false; // Oyun arka planda çalışmaya devam etsin
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
                      }

