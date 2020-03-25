package io.github.synthrose.artofalchemy;

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
	
	public int getTier() {
		return tier;
	}
	
	static MateriaRank ofTier(int tier) {
		for (int i = 0; i < values().length; i++) {
			MateriaRank rank = values()[i];
			if (tier == rank.getTier()) {
				return rank;
			}
		}
		return null;
	}
	
}
