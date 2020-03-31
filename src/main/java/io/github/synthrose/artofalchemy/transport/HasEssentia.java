package io.github.synthrose.artofalchemy.transport;

import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;

public interface HasEssentia {
	
	EssentiaContainer getContainer(int id);
	int getNumContainers();
	
	default EssentiaContainer getContainer() {
		return getContainer(0);
	}

}
