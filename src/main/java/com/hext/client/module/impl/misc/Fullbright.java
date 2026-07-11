package com.hext.client.module.impl.misc;

import com.hext.client.module.Module;
import com.hext.client.module.category.Category;
import com.hext.client.module.setting.SliderSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

import java.lang.reflect.Field;

public class Fullbright extends Module {

    private final SliderSetting brightness = addSetting(new SliderSetting(
            "Brightness", "Parlaklık seviyesi", 10.0, 1.0, 10.0, 0.5
    ));

    private double oldGamma = 1.0;

    // Vanilla'nın SimpleOption.setValue() metodu, verilen değeri kendi izin
    // verdiği aralıkta (gamma için 0.0-1.0) doğrular; aralık dışı bir değer
    // gönderilirse SESSİZCE varsayılan değere (0.5) döner. Bu yüzden setValue(10.0)
    // çağrısı aslında parlaklığı DÜŞÜRÜYORDU. Bunu bypass etmek için, doğrulamadan
    // geçen setValue() yerine, SimpleOption'ın içindeki değeri reflection ile
    // doğrudan yazıyoruz.
    private static Field valueField;

    static {
        try {
            valueField = SimpleOption.class.getDeclaredField("value");
            valueField.setAccessible(true);
        } catch (Exception e) {
            valueField = null;
        }
    }

    public Fullbright() {
        super("Fullbright", "Gamma değerini artırarak karanlığı kaldırır", Category.MISC);
    }

    private void forceGamma(double val) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options == null) return;
        SimpleOption<Double> gammaOption = client.options.getGamma();

        if (valueField != null) {
            try {
                valueField.set(gammaOption, val);
                return;
            } catch (Exception ignored) {
                // reflection başarısız olursa aşağıdaki fallback'e düşer
            }
        }
        // Fallback: bu, vanilla doğrulaması yüzünden 1.0'ı geçemez,
        // yani tam fullbright vermez ama en azından maksimuma çeker.
        gammaOption.setValue(Math.min(val, 1.0));
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options != null) {
            oldGamma = client.options.getGamma().getValue();
            forceGamma(brightness.getValue());
        }
    }

    @Override
    public void onDisable() {
        forceGamma(oldGamma);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (!isEnabled()) return;
        forceGamma(brightness.getValue());
    }
}
