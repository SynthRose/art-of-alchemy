package net.synthrose.artofalchemy;

import net.fabricmc.api.ClientModInitializer;

import net.synthrose.artofalchemy.gui.AoAScreens;
import net.synthrose.artofalchemy.network.AoAClientNetworking;
import net.synthrose.artofalchemy.render.AoARenderers;

public class ArtOfAlchemyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
    	AoAScreens.registerScreens();
    	AoARenderers.registerRenderers();
    	AoAClientNetworking.initializeClientNetworking();
    }

}