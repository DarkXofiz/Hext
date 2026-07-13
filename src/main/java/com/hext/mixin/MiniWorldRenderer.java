package com.hext.mixin;

import com.hext.HextClient;
import com.hext.modules.ESP;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    // Hook used by WorldRenderEvents, no need to inject here as we use Fabric API events.
}
