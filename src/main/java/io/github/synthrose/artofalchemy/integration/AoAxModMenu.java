package io.github.synthrose.artofalchemy.integration;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import io.github.synthrose.artofalchemy.AoAConfig;
import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class AoAxModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> AutoConfig.getConfigScreen(AoAConfig.class, screen).get();
    }
}
