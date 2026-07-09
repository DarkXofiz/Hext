package com.hext.client.gui.clickgui;

import com.hext.client.Hext;
import com.hext.client.gui.theme.ThemeManager;
import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.*;
import com.hext.client.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class ClickGUI extends Screen {

    private static final int PANEL_WIDTH   = 130;
    private static final int PANEL_HEADER  = 22;
    private static final int MODULE_HEIGHT = 20;
    private static final int SETTING_H     = 16;
    private static final int PANEL_SPACING = 8;
    private static final int PANEL_TOP     = 30;

    private boolean open = false;
    private final Map<Category, int[]> panelPositions = new LinkedHashMap<>();
    private final Map<Category, Boolean> collapsed = new HashMap<>();
    private final Map<String, Boolean> settingsOpen = new HashMap<>();

    // Drag state
    private Category dragging = null;
    private int dragOffX, dragOffY;

    // Animation: per-module hover progress [0..1]
    private final Map<String, Float> hoverAnim = new HashMap<>();
    // Animation: per-module enabled progress [0..1]
    private final Map<String, Float> enabledAnim = new HashMap<>();

    public ClickGUI() {
        super(Text.literal("HextClient"));
        initPanels();
    }

    private void initPanels() {
        int x = 8;
        for (Category cat : Category.values()) {
            panelPositions.put(cat, new int[]{x, PANEL_TOP});
            collapsed.put(cat, false);
            x += PANEL_WIDTH + PANEL_SPACING;
        }
    }

    public void open() {
        open = true;
        MinecraftClient.getInstance().setScreen(this);
    }

    public boolean isOpen() { return open; }

    @Override
    public void close() {
        open = false;
        MinecraftClient.getInstance().setScreen(null);
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ThemeManager theme = Hext.getInstance().getThemeManager();

        // Dim background
        ctx.fill(0, 0, width, height, 0x88000000);

        // Watermark at top
        drawWatermark(ctx, theme);

        // Render each category panel
        for (Category cat : Category.values()) {
            renderPanel(ctx, cat, mouseX, mouseY, delta, theme);
        }

        super.render(ctx, mouseX, mouseY, delta);
    }

    private void drawWatermark(DrawContext ctx, ThemeManager theme) {
        String wm = Hext.NAME + " v" + Hext.VERSION;
        int tx = width / 2 - textRenderer.getWidth(wm) / 2;
        RenderUtil.drawRoundedRect(ctx, tx - 6, 4, textRenderer.getWidth(wm) + 12, 16, 4, theme.bgSecondary);
        ctx.drawTextWithShadow(textRenderer, wm, tx, 8, theme.accentPrimary);
    }

    private void renderPanel(DrawContext ctx, Category cat, int mouseX, int mouseY, float delta, ThemeManager theme) {
        int[] pos = panelPositions.get(cat);
        int px = pos[0], py = pos[1];
        List<Module> mods = Hext.getInstance().getModuleManager().getModulesByCategory(cat);
        boolean isCollapsed = collapsed.getOrDefault(cat, false);

        int totalH = PANEL_HEADER;
        if (!isCollapsed) {
            for (Module mod : mods) {
                totalH += MODULE_HEIGHT;
                if (settingsOpen.getOrDefault(mod.getName(), false)) {
                    totalH += mod.getSettings().size() * SETTING_H + 4;
                }
            }
        }

        // Panel background
        RenderUtil.drawRoundedRect(ctx, px, py, PANEL_WIDTH, totalH, 6, theme.bgPrimary);

        // Header
        boolean headerHover = isHovering(mouseX, mouseY, px, py, PANEL_WIDTH, PANEL_HEADER);
        int headerColor = headerHover ? theme.accentSecondary : theme.accentPrimary;
        RenderUtil.drawRoundedRect(ctx, px, py, PANEL_WIDTH, PANEL_HEADER, 6, headerColor);
        ctx.drawTextWithShadow(textRenderer, cat.getIcon() + " " + cat.getName(),
                px + 7, py + 7, theme.textPrimary);

        // Collapse indicator
        String indicator = isCollapsed ? "+" : "-";
        ctx.drawTextWithShadow(textRenderer, indicator,
                px + PANEL_WIDTH - 12, py + 7, theme.textSecondary);

        if (!isCollapsed) {
            int my = py + PANEL_HEADER;
            for (Module mod : mods) {
                my = renderModule(ctx, mod, px, my, mouseX, mouseY, delta, theme);
            }
        }
    }

    private int renderModule(DrawContext ctx, Module mod, int px, int my,
                              int mouseX, int mouseY, float delta, ThemeManager theme) {
        boolean hover = isHovering(mouseX, mouseY, px, my, PANEL_WIDTH, MODULE_HEIGHT);
        String key = mod.getName();

        // Animate hover
        float hv = hoverAnim.getOrDefault(key, 0f);
        hv = RenderUtil.lerp(hv, hover ? 1f : 0f, 0.25f);
        hoverAnim.put(key, hv);

        // Animate enabled
        float ev = enabledAnim.getOrDefault(key, mod.isEnabled() ? 1f : 0f);
        ev = RenderUtil.lerp(ev, mod.isEnabled() ? 1f : 0f, 0.2f);
        enabledAnim.put(key, ev);

        // Background
        int bg = RenderUtil.lerpColor(theme.bgSecondary, theme.bgHover, hv);
        ctx.fill(px, my, px + PANEL_WIDTH, my + MODULE_HEIGHT, bg);

        // Enabled indicator bar
        if (ev > 0.01f) {
            int barColor = RenderUtil.withAlpha(theme.accentEnabled, (int)(ev * 200));
            ctx.fill(px, my, px + 3, my + MODULE_HEIGHT, barColor);
        }

        // Module name
        int textColor = mod.isEnabled() ? theme.textPrimary : theme.textSecondary;
        ctx.drawTextWithShadow(textRenderer, mod.getName(), px + 7, my + 6, textColor);

        // Settings arrow if has settings
        if (!mod.getSettings().isEmpty()) {
            boolean sOpen = settingsOpen.getOrDefault(key, false);
            ctx.drawTextWithShadow(textRenderer, sOpen ? "v" : ">",
                    px + PANEL_WIDTH - 12, my + 6, theme.textMuted);
        }

        my += MODULE_HEIGHT;

        // Settings panel
        if (settingsOpen.getOrDefault(key, false)) {
            for (Setting<?> setting : mod.getSettings()) {
                my = renderSetting(ctx, setting, mod, px, my, mouseX, mouseY, theme);
            }
            my += 4;
        }

        return my;
    }

    private int renderSetting(DrawContext ctx, Setting<?> setting, Module mod,
                               int px, int my, int mouseX, int mouseY, ThemeManager theme) {
        ctx.fill(px, my, px + PANEL_WIDTH, my + SETTING_H, theme.bgTertiary);
        ctx.drawTextWithShadow(textRenderer, setting.getName(), px + 10, my + 4, theme.textSecondary);

        String valStr = setting.getDisplayValue();
        int valX = px + PANEL_WIDTH - textRenderer.getWidth(valStr) - 5;
        ctx.drawTextWithShadow(textRenderer, valStr, valX, my + 4, theme.accentPrimary);

        // Slider bar
        if (setting instanceof SliderSetting slider) {
            int barX = px + 10;
            int barW = PANEL_WIDTH - 20;
            int barY = my + SETTING_H - 3;
            ctx.fill(barX, barY, barX + barW, barY + 2, theme.bgHover);
            ctx.fill(barX, barY, barX + (int)(barW * slider.getPercentage()), barY + 2, theme.accentPrimary);
        }

        return my + SETTING_H;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int mx = (int) mouseX, my = (int) mouseY;

        for (Category cat : Category.values()) {
            int[] pos = panelPositions.get(cat);
            int px = pos[0], py = pos[1];

            // Header click — drag init or collapse
            if (isHovering(mx, my, px, py, PANEL_WIDTH, PANEL_HEADER)) {
                if (button == 0) {
                    dragging = cat;
                    dragOffX = mx - px;
                    dragOffY = my - py;
                } else if (button == 1) {
                    collapsed.put(cat, !collapsed.getOrDefault(cat, false));
                }
                return true;
            }

            if (collapsed.getOrDefault(cat, false)) continue;

            List<Module> mods = Hext.getInstance().getModuleManager().getModulesByCategory(cat);
            int ry = py + PANEL_HEADER;
            for (Module mod : mods) {
                if (isHovering(mx, my, px, ry, PANEL_WIDTH, MODULE_HEIGHT)) {
                    if (button == 0) mod.toggle();
                    else if (button == 1 && !mod.getSettings().isEmpty()) {
                        boolean cur = settingsOpen.getOrDefault(mod.getName(), false);
                        settingsOpen.put(mod.getName(), !cur);
                    }
                    return true;
                }
                ry += MODULE_HEIGHT;

                if (settingsOpen.getOrDefault(mod.getName(), false)) {
                    for (Setting<?> s : mod.getSettings()) {
                        if (isHovering(mx, my, px, ry, PANEL_WIDTH, SETTING_H)) {
                            handleSettingClick(s, mx, px, button);
                            return true;
                        }
                        ry += SETTING_H;
                    }
                    ry += 4;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void handleSettingClick(Setting<?> s, int mx, int px, int button) {
        if (s instanceof BooleanSetting b && button == 0) {
            b.toggle();
        } else if (s instanceof ModeSetting m && button == 0) {
            m.cycle();
        } else if (s instanceof SliderSetting slider && button == 0) {
            int barX = px + 10, barW = PANEL_WIDTH - 20;
            float pct = (float)(mx - barX) / barW;
            slider.setValueClamped(slider.getMin() + (slider.getMax() - slider.getMin()) * pct);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (dragging != null && button == 0) {
            panelPositions.get(dragging)[0] = (int) mouseX - dragOffX;
            panelPositions.get(dragging)[1] = (int) mouseY - dragOffY;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) dragging = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_X) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private boolean isHovering(int mx, int my, int x, int y, int w, int h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }
}
