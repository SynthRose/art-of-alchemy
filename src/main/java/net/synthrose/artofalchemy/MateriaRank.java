package net.synthrose.artofalchemy;

public enum MateriaRank {
	OMEGA (7),
	S     (6),
	A     (5),
	B     (4),
	C     (3),
	D     (2),
	E     (1),
	F     (0);
	
	private int tier;
	
	MateriaRank(int tier) {
		this.tier = tier;
	}
	
	static MateriaRank byTier(int tier) {
		switch(tier) {
			case 7:  return OMEGA;
			case 6:  return S;
			case 5:  return A;
			case 4:  return B;
			case 3:  return C;
			case 2:  return D;
			case 1:  return E;
			default: return F;
		}
	}
	
	public int getTier() {
		return tier;
	}
}
