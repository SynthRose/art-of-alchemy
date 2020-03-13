package net.synthrose.artofalchemy;

import net.fabricmc.api.ClientModInitializer;
import net.synthrose.artofalchemy.gui.Guis;

public class ArtOfAlchemyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
    	Guis.registerScreens();
    }

}