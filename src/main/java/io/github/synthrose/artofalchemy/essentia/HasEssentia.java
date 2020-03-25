package io.github.synthrose.artofalchemy.essentia;

public interface HasEssentia {
	
	public EssentiaContainer getContainer(int id);
	public int getNumContainers();
	
	public default EssentiaContainer getContainer() {
		return getContainer(0);
	}

}
