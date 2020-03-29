package io.github.synthrose.artofalchemy;

import io.github.synthrose.artofalchemy.gui.screen.AoAScreens;
import io.github.synthrose.artofalchemy.network.AoAClientNetworking;
import io.github.synthrose.artofalchemy.render.AoARenderers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ArtOfAlchemyClient implements ClientModInitializer {

    @Environment(EnvType.CLIENT)
    @Override
    public void onInitializeClient() {
    	AoAScreens.registerScreens();
    	AoARenderers.registerRenderers();
    	AoAClientNetworking.initializeClientNetworking();
    }

}