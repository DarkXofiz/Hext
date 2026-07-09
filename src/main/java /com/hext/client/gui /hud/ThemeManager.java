package com.hextclient.client.gui.theme;

public class ThemeManager {

    // Background colors
    public int bgPrimary   = 0xE5141414;
    public int bgSecondary = 0xE51E1E1E;
    public int bgTertiary  = 0xE5282828;
    public int bgHover     = 0xE5323232;

    // Accent colors
    public int accentPrimary   = 0xFF5865F2;
    public int accentSecondary = 0xFF4752C4;
    public int accentEnabled   = 0xFF43B581;
    public int accentDisabled  = 0xFF747F8D;

    // Text colors
    public int textPrimary   = 0xFFFFFFFF;
    public int textSecondary = 0xFFB9BBBE;
    public int textMuted     = 0xFF72767D;

    // Border
    public int border = 0xFF3D3D3D;

    // Scrollbar
    public int scrollbar = 0xFF4F545C;

    // Animation speed
    public float animSpeed = 0.15f;

    private String currentTheme = "Hext";

    public void setTheme(String theme) {
        this.currentTheme = theme;
        switch (theme) {
            case "Purple" -> {
                accentPrimary   = 0xFF9B59B6;
                accentSecondary = 0xFF7D3C98;
                accentEnabled   = 0xFF43B581;
            }
            case "Red" -> {
                accentPrimary   = 0xFFE74C3C;
                accentSecondary = 0xFFC0392B;
                accentEnabled   = 0xFF43B581;
            }
            case "Cyan" -> {
                accentPrimary   = 0xFF1ABC9C;
                accentSecondary = 0xFF17A589;
                accentEnabled   = 0xFF43B581;
            }
            default -> {
                accentPrimary   = 0xFF5865F2;
                accentSecondary = 0xFF4752C4;
                accentEnabled   = 0xFF43B581;
            }
        }
    }

    public String getCurrentTheme() { return currentTheme; }
}
