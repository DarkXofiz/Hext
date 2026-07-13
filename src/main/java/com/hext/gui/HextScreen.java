package com.hext.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import com.hext.HextClient;
import com.hext.modules.BaseModule;
import com.hext.modules.ModuleSetting;

import java.util.*;

public class HextScreen extends Screen {
    private static final int PANEL_WIDTH = 110;
    private static final int SETTING_HEIGHT = 16;
    private static final int MODULE_HEIGHT = 16;
    
    private static final int COLOR_BG = 0x0d0d0d;
    private static final int COLOR_PANEL_BG = 0x191919;
    private static final int COLOR_ACCENT = 0x7c3aed;
    private static final int COLOR_ACCENT_DARK = 0x6d28d9;
    private static final int COLOR_ENABLED = 0x10b981;
    private static final int COLOR_DISABLED = 0xef4444;
    private static final int COLOR_TEXT = 0xffffff;
    private static final int COLOR_TEXT_GRAY = 0x9ca3af;

    private enum Category {
        COMBAT("Combat", 0xff6b6b),
        MOVEMENT("Movement", 0x4ecdc4),
        PLAYER("Player", 0x45b7d1),
        RENDER("Render", 0xf9ca24),
        MISC("Misc", 0x6c5ce7);

        public final String name;
        public final int color;
        Category(String name, int color) {
            this.name = name;
            this.color = color;
        }
    }

    private Map<Category, List<BaseModule>> categoryModules = new LinkedHashMap<>();
    private Map<BaseModule, Boolean> expandedModules = new HashMap<>();
    private Category selectedCategory = Category.COMBAT;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private boolean draggingPanel = false;
    private int panelX = 10;
    private int panelY = 30;

    public HextScreen() {
        super(Text.literal("Hext Client"));
        initializeCategories();
    }

    private void initializeCategories() {
        for (Category cat : Category.values()) {
            categoryModules.put(cat, new ArrayList<>());
        }
        
        if (HextClient.modules != null) {
            for (BaseModule module : HextClient.modules) {
                if (module == null) continue;
                
                String name = module.name.toLowerCase();
                if (name.contains("fly") || name.contains("aura") || name.contains("trigger")) {
                    categoryModules.get(Category.COMBAT).add(module);
                } else if (name.contains("elytra") || name.contains("speed")) {
                    categoryModules.get(Category.MOVEMENT).add(module);
                } else {
                    categoryModules.get(Category.MISC).add(module);
                }
                
                expandedModules.put(module, false);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, COLOR_BG);
        
        renderCategoryTabs(context, mouseX, mouseY);
        renderModulePanel(context, mouseX, mouseY);
    }

    private void renderCategoryTabs(DrawContext context, int mouseX, int mouseY) {
        int tabX = panelX;
        int tabY = panelY;
        int tabHeight = 22;
        
        context.fill(tabX, tabY, tabX + PANEL_WIDTH, tabY + tabHeight, COLOR_PANEL_BG);
        context.fill(tabX, tabY + tabHeight - 2, tabX + PANEL_WIDTH, tabY + tabHeight, COLOR_ACCENT);
        
        context.drawCenteredTextWithShadow(this.textRenderer, 
                "§l§dCATEGORIES", 
                tabX + PANEL_WIDTH / 2, tabY + 6, COLOR_ACCENT);
        
        int catY = tabY + tabHeight + 2;
        for (Category cat : Category.values()) {
            boolean isSelected = cat == selectedCategory;
            int catColor = isSelected ? 0xffffff : COLOR_TEXT_GRAY;
            int bgColor = isSelected ? COLOR_ACCENT : COLOR_PANEL_BG;
            
            boolean hover = mouseX >= tabX && mouseX <= tabX + PANEL_WIDTH && 
                          mouseY >= catY && mouseY <= catY + 18;
            
            if (hover && !isSelected) bgColor = 0x2a2a2a;
            
            context.fill(tabX, catY, tabX + PANEL_WIDTH, catY + 18, bgColor);
            
            if (isSelected) {
                context.fill(tabX, catY, tabX + 3, catY + 18, cat.color);
            }
            
            context.drawCenteredTextWithShadow(this.textRenderer, 
                    "§" + getColorCode(cat.color) + cat.name, 
                    tabX + PANEL_WIDTH / 2, catY + 4, catColor);
            
            catY += 18;
        }
    }

    private void renderModulePanel(DrawContext context, int mouseX, int mouseY) {
        int panelStartX = panelX + PANEL_WIDTH + 5;
        int panelStartY = panelY;
        int panelWidth = 180;
        int panelHeight = 300;
        
        // Panel header
        context.fill(panelStartX, panelStartY, panelStartX + panelWidth, panelStartY + 25, COLOR_PANEL_BG);
        context.fill(panelStartX, panelStartY + 23, panelStartX + panelWidth, panelStartY + 25, COLOR_ACCENT);
        
        context.drawTextWithShadow(this.textRenderer, 
                "§l§d◆ " + selectedCategory.name.toUpperCase(), 
                panelStartX + 8, panelStartY + 7, COLOR_ACCENT);
        
        // Modules list
        int modY = panelStartY + 28;
        List<BaseModule> modules = categoryModules.get(selectedCategory);
        
        context.fill(panelStartX, panelStartY + 25, panelStartX + panelWidth, panelStartY + panelHeight, COLOR_PANEL_BG);
        
        if (modules != null) {
            for (BaseModule module : modules) {
                if (module == null) continue;
                
                boolean isExpanded = expandedModules.getOrDefault(module, false);
                
                // Module header
                renderModuleHeader(context, module, panelStartX + 6, modY, panelWidth - 12, mouseX, mouseY);
                modY += MODULE_HEIGHT + 2;
                
                // Module settings (if expanded)
                if (isExpanded && module.getSettings() != null) {
                    for (ModuleSetting setting : module.getSettings()) {
                        if (setting == null) continue;
                        
                        renderSetting(context, setting, panelStartX + 12, modY, panelWidth - 24, mouseX, mouseY);
                        modY += SETTING_HEIGHT + 2;
                    }
                    modY += 4;
                }
            }
        }
    }

    private void renderModuleHeader(DrawContext context, BaseModule module, int x, int y, int w, int mouseX, int mouseY) {
        boolean isExpanded = expandedModules.getOrDefault(module, false);
        boolean isHovered = mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + MODULE_HEIGHT;
        
        // Background
        int bgColor = isHovered ? 0x2a2a2a : COLOR_PANEL_BG;
        context.fill(x, y, x + w, y + MODULE_HEIGHT, bgColor);
        
        // Left border (status)
        int statusColor = module.enabled ? COLOR_ENABLED : COLOR_DISABLED;
        context.fill(x, y, x + 2, y + MODULE_HEIGHT, statusColor);
        
        // Expand/collapse indicator
        String indicator = isExpanded ? "▼" : "▶";
        context.drawTextWithShadow(this.textRenderer, "§7" + indicator, x + 4, y + 3, COLOR_TEXT_GRAY);
        
        // Module name
        String nameColor = module.enabled ? "§a" : "§c";
        context.drawTextWithShadow(this.textRenderer, 
                nameColor + module.name, 
                x + 15, y + 3, COLOR_TEXT);
    }

    private void renderSetting(DrawContext context, ModuleSetting setting, int x, int y, int w, int mouseX, int mouseY) {
        context.drawTextWithShadow(this.textRenderer, 
                "§7" + setting.name, 
                x, y, COLOR_TEXT_GRAY);
        
        if (setting.type == ModuleSetting.Type.BOOLEAN) {
            renderBooleanSetting(context, setting, x + w - 35, y - 1, mouseX, mouseY);
        } else if (setting.type == ModuleSetting.Type.SLIDER) {
            renderSliderSetting(context, setting, x, y + 9, w, mouseX, mouseY);
        } else if (setting.type == ModuleSetting.Type.TEXT) {
            renderTextSetting(context, setting, x + w - 60, y - 1, 55, mouseX, mouseY);
        }
    }

    private void renderBooleanSetting(DrawContext context, ModuleSetting setting, int x, int y, int mouseX, int mouseY) {
        boolean value = (Boolean) setting.value;
        int color = value ? COLOR_ENABLED : COLOR_DISABLED;
        
        boolean hover = mouseX >= x && mouseX <= x + 30 && mouseY >= y && mouseY <= y + 12;
        int bgColor = hover ? 0x2a2a2a : 0x1a1a1a;
        
        context.fill(x, y, x + 30, y + 12, bgColor);
        
        String text = value ? "§aON" : "§cOFF";
        context.drawCenteredTextWithShadow(this.textRenderer, text, x + 15, y + 2, COLOR_TEXT);
    }

    private void renderSliderSetting(DrawContext context, ModuleSetting setting, int x, int y, int w, int mouseX, int mouseY) {
        double value = (Double) setting.value;
        double range = setting.max - setting.min;
        double percentage = (value - setting.min) / range;
        
        // Slider bg
        context.fill(x, y, x + w, y + 6, 0x1a1a1a);
        
        // Slider fill
        int fillW = (int) (w * percentage);
        context.fill(x, y, x + fillW, y + 6, COLOR_ACCENT);
        
        // Value
        String valueText = String.format("%.1f", value);
        context.drawTextWithShadow(this.textRenderer, "§7" + valueText, x + w + 5, y - 2, COLOR_TEXT_GRAY);
    }

    private void renderTextSetting(DrawContext context, ModuleSetting setting, int x, int y, int w, int mouseX, int mouseY) {
        String text = (String) setting.value;
        
        boolean hover = mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + 12;
        int bgColor = hover ? 0x2a2a2a : 0x1a1a1a;
        
        context.fill(x, y, x + w, y + 12, bgColor);
        context.drawTextWithShadow(this.textRenderer, "§7" + text, x + 3, y + 2, COLOR_TEXT_GRAY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Category selection
        int tabX = panelX;
        int tabY = panelY + 22;
        for (Category cat : Category.values()) {
            if (mouseX >= tabX && mouseX <= tabX + PANEL_WIDTH && 
                mouseY >= tabY && mouseY <= tabY + 18) {
                selectedCategory = cat;
                return true;
            }
            tabY += 18;
        }
        
        // Module expand/collapse
        int panelStartX = panelX + PANEL_WIDTH + 5;
        int modY = panelStartY + 28;
        List<BaseModule> modules = categoryModules.get(selectedCategory);
        
        if (modules != null) {
            for (BaseModule module : modules) {
                if (module == null) continue;
                
                if (mouseX >= panelStartX + 6 && mouseX <= panelStartX + panelWidth - 6 && 
                    mouseY >= modY && mouseY <= modY + MODULE_HEIGHT) {
                    
                    if (button == 0) {
                        // Left click - toggle expand
                        expandedModules.put(module, !expandedModules.get(module));
                    } else if (button == 1) {
                        // Right click - toggle module
                        module.toggle();
                    }
                    return true;
                }
                
                boolean isExpanded = expandedModules.getOrDefault(module, false);
                modY += MODULE_HEIGHT + 2;
                
                if (isExpanded && module.getSettings() != null) {
                    for (ModuleSetting setting : module.getSettings()) {
                        if (setting == null) continue;
                        
                        if (setting.type == ModuleSetting.Type.BOOLEAN) {
                            if (mouseX >= panelStartX + panelWidth - 41 && mouseX <= panelStartX + panelWidth - 11 &&
                                mouseY >= modY - 1 && mouseY <= modY + 11) {
                                setting.value = !(Boolean) setting.value;
                                com.hext.config.Config.save();
                                return true;
                            }
                        }
                        modY += SETTING_HEIGHT + 2;
                    }
                    modY += 4;
                }
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            int panelStartX = panelX + PANEL_WIDTH + 5;
            int modY = panelStartY + 28;
            List<BaseModule> modules = categoryModules.get(selectedCategory);
            
            if (modules != null) {
                for (BaseModule module : modules) {
                    if (module == null) continue;
                    
                    boolean isExpanded = expandedModules.getOrDefault(module, false);
                    modY += MODULE_HEIGHT + 2;
                    
                    if (isExpanded && module.getSettings() != null) {
                        for (ModuleSetting setting : module.getSettings()) {
                            if (setting == null) continue;
                            
                            if (setting.type == ModuleSetting.Type.SLIDER) {
                                int sliderX = panelStartX + 12;
                                int sliderW = 168 - 24;
                                
                                if (mouseX >= sliderX && mouseX <= sliderX + sliderW) {
                                    double percentage = (mouseX - sliderX) / sliderW;
                                    percentage = Math.max(0, Math.min(1, percentage));
                                    
                                    double value = setting.min + (setting.max - setting.min) * percentage;
                                    setting.value = value;
                                    com.hext.config.Config.save();
                                    return true;
                                }
                            }
                            modY += SETTING_HEIGHT + 2;
                        }
                    }
                }
            }
        }
        
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private String getColorCode(int color) {
        if (color == 0x7c3aed) return "d"; // Purple
        if (color == 0xff6b6b) return "c"; // Red
        if (color == 0x4ecdc4) return "b"; // Cyan
        if (color == 0x45b7d1) return "3"; // Light blue
        if (color == 0xf9ca24) return "e"; // Yellow
        return "f"; // White
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
