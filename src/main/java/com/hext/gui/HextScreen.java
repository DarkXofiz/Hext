package com.hext.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import com.hext.HextClient;
import com.hext.modules.BaseModule;
import com.hext.modules.ModuleSetting;

public class HextScreen extends Screen {
    private static final int SETTING_H = 25;
    private static final int MARGIN = 10;
    private static final int MODULE_H = 25;
    private static final int COLOR_BG = 0x1a1a1a;
    private static final int COLOR_ENABLED = 0x00ff00;
    private static final int COLOR_DISABLED = 0xff0000;
    private static final int COLOR_HEADER = 0xffff00;
    private static final int COLOR_BUTTON = 0x333333;

    private BaseModule selectedModule = null;
    private ModuleSetting draggedSlider = null;
    private int sliderStartX = 0;

    public HextScreen() {
        super(Text.literal("Hext Client"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        
        // Başlık
        context.drawCenteredTextWithShadow(this.textRenderer, 
                "§l§6Hext Client", 
                this.width / 2, MARGIN, 0xFFFFFF);

        // Modules listesi (sol taraf)
        int moduleY = MARGIN + 20;
        context.fill(MARGIN, moduleY - 5, MARGIN + 150, this.height - MARGIN, COLOR_BG);
        
        context.drawTextWithShadow(this.textRenderer, "§bModules:", MARGIN + 5, moduleY, COLOR_HEADER);
        moduleY += 15;

        if (HextClient.modules != null) {
            for (BaseModule module : HextClient.modules) {
                if (module == null) continue;

                String text = module.name;
                int color = module.enabled ? COLOR_ENABLED : COLOR_DISABLED;
                
                // Highlight selected
                if (module == selectedModule) {
                    context.fill(MARGIN, moduleY - 2, MARGIN + 145, moduleY + MODULE_H - 2, 0x444444);
                }
                
                context.drawTextWithShadow(this.textRenderer, text, MARGIN + 10, moduleY, color);
                moduleY += MODULE_H;
            }
        }

        // Settings (sağ taraf)
        if (selectedModule != null) {
            int settingsX = MARGIN + 170;
            context.fill(settingsX, MARGIN + 15, this.width - MARGIN, this.height - MARGIN, COLOR_BG);
            
            context.drawTextWithShadow(this.textRenderer, 
                    "§b" + selectedModule.name, 
                    settingsX + 10, MARGIN + 20, COLOR_HEADER);
            
            int settingsY = MARGIN + 40;

            // Module toggle button
            int toggleX = settingsX + 10;
            int toggleW = 100;
            String toggleText = selectedModule.enabled ? "§a[ENABLED]" : "§c[DISABLED]";
            context.fill(toggleX, settingsY, toggleX + toggleW, settingsY + 20, COLOR_BUTTON);
            context.drawCenteredTextWithShadow(this.textRenderer, toggleText, toggleX + toggleW / 2, settingsY + 6, 0xFFFFFF);
            settingsY += 30;

            // Settings
            if (selectedModule.getSettings() != null && !selectedModule.getSettings().isEmpty()) {
                for (ModuleSetting setting : selectedModule.getSettings()) {
                    if (setting == null) continue;

                    renderSetting(context, setting, settingsX + 10, settingsY, this.width - MARGIN - 20, mouseX, mouseY);
                    settingsY += SETTING_H + 5;
                }
            }
        }

        // Footer
        context.drawTextWithShadow(this.textRenderer, "§8Press ESC to close | Left Click: Toggle/Select", 
                MARGIN, this.height - MARGIN - 10, 0x888888);

        super.render(context, mouseX, mouseY, delta);
    }

    private void renderSetting(DrawContext context, ModuleSetting setting, int x, int y, int maxWidth, int mouseX, int mouseY) {
        context.drawTextWithShadow(this.textRenderer, setting.name + ":", x, y, 0xFFFFFF);

        if (setting.type == ModuleSetting.Type.BOOLEAN) {
            renderBooleanSetting(context, setting, x, y, mouseX, mouseY);
        } else if (setting.type == ModuleSetting.Type.SLIDER) {
            renderSliderSetting(context, setting, x, y, maxWidth, mouseX, mouseY);
        } else if (setting.type == ModuleSetting.Type.TEXT) {
            renderTextSetting(context, setting, x, y, mouseX, mouseY);
        }
    }

    private void renderBooleanSetting(DrawContext context, ModuleSetting setting, int x, int y, int mouseX, int mouseY) {
        int buttonX = x + 150;
        int buttonY = y + 5;
        int buttonW = 50;
        int buttonH = 15;

        boolean value = (Boolean) setting.value;
        int color = value ? COLOR_ENABLED : COLOR_DISABLED;
        
        // Button background
        context.fill(buttonX, buttonY, buttonX + buttonW, buttonY + buttonH, COLOR_BUTTON);
        
        // Button text
        String text = value ? "ON" : "OFF";
        context.drawCenteredTextWithShadow(this.textRenderer, "§f" + text, buttonX + buttonW / 2, buttonY + 3, color);

        // Hover effect
        if (mouseX >= buttonX && mouseX <= buttonX + buttonW && mouseY >= buttonY && mouseY <= buttonY + buttonH) {
            context.fill(buttonX, buttonY, buttonX + buttonW, buttonY + buttonH, 0x555555);
        }
    }

    private void renderSliderSetting(DrawContext context, ModuleSetting setting, int x, int y, int maxWidth, int mouseX, int mouseY) {
        int sliderX = x + 150;
        int sliderY = y + 8;
        int sliderW = maxWidth - 150;
        int sliderH = 10;

        // Slider background
        context.fill(sliderX, sliderY, sliderX + sliderW, sliderY + sliderH, COLOR_BUTTON);

        // Calculate slider position
        double value = (Double) setting.value;
        double range = setting.max - setting.min;
        double percentage = (value - setting.min) / range;
        int handleX = sliderX + (int) (sliderW * percentage);

        // Slider handle
        context.fill(handleX - 3, sliderY - 2, handleX + 3, sliderY + sliderH + 2, COLOR_ENABLED);

        // Value text
        context.drawTextWithShadow(this.textRenderer, String.format("%.2f", value), x + 150, y + 20, 0xAAAAAA);

        // Hover effect
        if (mouseX >= sliderX && mouseX <= sliderX + sliderW && mouseY >= sliderY - 5 && mouseY <= sliderY + sliderH + 5) {
            context.fill(sliderX, sliderY, sliderX + sliderW, sliderY + sliderH, 0x444444);
        }
    }

    private void renderTextSetting(DrawContext context, ModuleSetting setting, int x, int y, int mouseX, int mouseY) {
        int inputX = x + 150;
        int inputY = y + 5;
        int inputW = 100;
        int inputH = 15;

        String text = (String) setting.value;

        // Input background
        context.fill(inputX, inputY, inputX + inputW, inputY + inputH, COLOR_BUTTON);
        
        // Text
        context.drawTextWithShadow(this.textRenderer, text, inputX + 5, inputY + 4, 0xFFFFFF);

        // Hover effect
        if (mouseX >= inputX && mouseX <= inputX + inputW && mouseY >= inputY && mouseY <= inputY + inputH) {
            context.fill(inputX, inputY, inputX + inputW, inputY + inputH, 0x555555);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        // Module toggle butonuna tıklama
        if (selectedModule != null) {
            int settingsX = MARGIN + 170;
            int toggleX = settingsX + 10;
            int toggleY = MARGIN + 40;
            int toggleW = 100;

            if (mouseX >= toggleX && mouseX <= toggleX + toggleW && 
                mouseY >= toggleY && mouseY <= toggleY + 20) {
                selectedModule.toggle();
                return true;
            }

            // Settings tıklama
            int settingsY = MARGIN + 70;
            for (ModuleSetting setting : selectedModule.getSettings()) {
                if (setting == null) continue;

                if (setting.type == ModuleSetting.Type.BOOLEAN) {
                    int buttonX = settingsX + 160;
                    int buttonY = settingsY + 5;
                    if (mouseX >= buttonX && mouseX <= buttonX + 50 && mouseY >= buttonY && mouseY <= buttonY + 15) {
                        setting.value = !(Boolean) setting.value;
                        com.hext.config.Config.save();
                        return true;
                    }
                } else if (setting.type == ModuleSetting.Type.SLIDER) {
                    int sliderX = settingsX + 160;
                    int sliderY = settingsY + 8;
                    int sliderW = this.width - MARGIN * 2 - 180;
                    if (mouseX >= sliderX && mouseX <= sliderX + sliderW && 
                        mouseY >= sliderY - 5 && mouseY <= sliderY + 15) {
                        draggedSlider = setting;
                        sliderStartX = (int) mouseX;
                        return true;
                    }
                }

                settingsY += SETTING_H + 5;
            }
        }

        // Module seçimi
        int moduleY = MARGIN + 35;
        for (BaseModule module : HextClient.modules) {
            if (module == null) continue;

            if (mouseX >= MARGIN && mouseX <= MARGIN + 150 && 
                mouseY >= moduleY && mouseY < moduleY + MODULE_H) {
                selectedModule = module;
                return true;
            }
            moduleY += MODULE_H;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggedSlider != null && button == 0) {
            int settingsX = MARGIN + 170;
            int sliderX = settingsX + 160;
            int sliderW = this.width - MARGIN * 2 - 180;

            double percentage = (mouseX - sliderX) / sliderW;
            percentage = Math.max(0, Math.min(1, percentage));

            double value = draggedSlider.min + (draggedSlider.max - draggedSlider.min) * percentage;
            draggedSlider.value = value;
            com.hext.config.Config.save();
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            draggedSlider = null;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}
