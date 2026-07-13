package com.hext.modules;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

public class Hitbox extends BaseModule {
    public Hitbox() { super("Hitbox"); }

    // Hitbox genişletme Mixin üzerinden yapılır (MixinEntity)
    // Bu sınıf yalnızca toggle durumunu tutar
    public void onRender(WorldRenderContext context) {
        // Görsel ESP yok, Mixin zaten bounding box'ı genişletiyor
    }
}
