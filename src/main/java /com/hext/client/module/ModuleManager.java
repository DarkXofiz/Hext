package com.hext.client.module;

import com.hext.client.module.impl.combat.Hitbox;
import com.hext.client.module.impl.combat.Reach;
import com.hext.client.module.impl.misc.Fullbright;
import com.hext.client.module.impl.misc.NameTags;
import com.hext.client.module.impl.misc.NoHurtCam;
import com.hext.client.module.impl.movement.AutoJump;
import com.hext.client.module.impl.movement.AutoSprint;
import com.hext.client.module.impl.movement.FreeLook;
import com.hext.client.module.impl.movement.ToggleSprint;
import com.hext.client.module.impl.player.ArmorStatus;
import com.hext.client.module.impl.render.Keystrokes;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.*;
import java.util.stream.Collectors;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        register(
            // Combat
            new Hitbox(),
            new Reach(),
            // Movement
            new ToggleSprint(),
            new AutoSprint(),
            new FreeLook(),
            new AutoJump(),
            // Render
            new Keystrokes(),
            // Player
            new ArmorStatus(),
            // Misc
            new Fullbright(),
            new NoHurtCam(),
            new NameTags()
        );
    }

    private void register(Module... mods) {
        modules.addAll(Arrays.asList(mods));
    }

    public void onTick(MinecraftClient client) {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.onTick(client);
            }
        }
    }

    public void onKeyPress(int key) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return;
        for (Module module : modules) {
            if (module.getKeybind() == key) {
                module.toggle();
            }
        }
    }

    public Optional<Module> getModule(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public <T extends Module> Optional<T> getModule(Class<T> clazz) {
        return modules.stream()
                .filter(m -> m.getClass() == clazz)
                .map(clazz::cast)
                .findFirst();
    }

    public List<Module> getModules() { return Collections.unmodifiableList(modules); }

    public List<Module> getModulesByCategory(com.hext.client.module.category.Category category) {
        return modules.stream()
                .filter(m -> m.getCategory() == category)
                .sorted(Comparator.comparing(Module::getName))
                .collect(Collectors.toList());
    }
}
