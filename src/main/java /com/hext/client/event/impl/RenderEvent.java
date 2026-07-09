package com.hextclient.client.event.impl;

import net.minecraft.client.gui.DrawContext;

public class RenderEvent {
    private final DrawContext drawContext;
    private final float tickDelta;

    public RenderEvent(DrawContext drawContext, float tickDelta) {
        this.drawContext = drawContext;
        this.tickDelta = tickDelta;
    }

    public DrawContext getDrawContext() { return drawContext; }
    public float getTickDelta() { return tickDelta; }
}
