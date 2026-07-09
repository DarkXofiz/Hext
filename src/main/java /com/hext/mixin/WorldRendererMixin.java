package com.hextclient.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    // Reserved for world-level render hooks (e.g., custom sky color, ESP outlines)
}
