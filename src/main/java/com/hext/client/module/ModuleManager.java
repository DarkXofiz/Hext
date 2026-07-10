package com.hext.client.module;

import com.hext.client.module.category.Category;
import com.hext.client.module.impl.combat.HitBox;
import com.hext.client.module.impl.combat.Reach;
import com.hext.client.module.impl.movement.AutoJump;
import com.hext.client.module.impl.movement.AutoSprint;
import com.hext.client.module.impl.movement.FreeLook;
import com.hext.client.module.impl.movement.ToggleSprint;
import com.hext.client.module.impl.misc.Fullbright;
import com.hext.client.module.impl.misc.NameTags;
import com.hext.client.module.impl.misc.NoHurtCam;
import com.hext.client.module.impl.render.Keystrokes;
import com.hext.client.module.impl.player.ArmorStatus;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        register(
                new HitBox(),
                new Reach(),
                new AutoJump(),
                new AutoSprint(),
                new FreeLook(),
                new ToggleSprint(),
                new Fullbright(),
                new NameTags(),
                new NoHurtCam(),
                new Keystrokes(),
                new ArmorStatus()
        );
    }

    private void register(Module... mods) {
        for (Module mod : mods) {
            modules.add(mod);
        }
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Category category) {
        return modules.stream()
                .filter(mod -> mod.getCategory() == category)
                .collect(Collectors.toList());
    }

    public Optional<Module> getModule(String name) {
        return modules.stream()
                .filter(mod -> mod.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> Optional<T> getModule(Class<T> clazz) {
        return modules.stream()
                .filter(clazz::isInstance)
                .map(mod -> (T) mod)
                .findFirst();
    }

    public void onKeyPress(int key) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) {
            for (Module mod : modules) {
                mod.setToggling(false);
            }
            return;
        }

        for (Module mod : modules) {
            if (mod.getKeybind() == key) {
                if (!mod.isToggling()) {
                    mod.setToggling(true);
                    mod.toggle();
                }
                return;
            }
        }
    }
}
