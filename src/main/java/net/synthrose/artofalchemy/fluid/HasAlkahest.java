package net.synthrose.artofalchemy.fluid;

public interface HasAlkahest {
	
	public int getAlkahest();
	
	public boolean setAlkahest(int amount);
	
	public default boolean hasAlkahest() {
		return getAlkahest() > 0;
	};
	
	public default boolean addAlkahest(int amount) {
		return setAlkahest(getAlkahest() + amount);
	}

}
