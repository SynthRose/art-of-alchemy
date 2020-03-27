package io.github.synthrose.artofalchemy.essentia;

public interface HasEssentia {
	
	EssentiaContainer getContainer(int id);
	int getNumContainers();
	
	default EssentiaContainer getContainer() {
		return getContainer(0);
	}

}
