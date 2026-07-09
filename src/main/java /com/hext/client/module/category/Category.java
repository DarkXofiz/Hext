package com.hextclient.client.module.category;

public enum Category {
    COMBAT("Combat", "\u2694"),
    MOVEMENT("Movement", "\u27A4"),
    RENDER("Render", "\u2728"),
    PLAYER("Player", "\u25B6"),
    MISC("Misc", "\u2699");

    private final String name;
    private final String icon;

    Category(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() { return name; }
    public String getIcon() { return icon; }
}
