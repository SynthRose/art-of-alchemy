package io.github.synthrose.artofalchemy;

import net.minecraft.util.Rarity;

public enum MateriaRank {
	OMEGA (7, Rarity.EPIC),
	S     (6, Rarity.RARE),
	A     (5, Rarity.UNCOMMON),
	B     (4),
	C     (3),
	D     (2),
	E     (1),
	F     (0);
	
	public final int tier;
	public final Rarity rarity;

	MateriaRank(int tier) {
		this.tier = tier;
		this.rarity = Rarity.COMMON;
	}

	MateriaRank(int tier, Rarity rarity) {
		this.tier = tier;
		this.rarity = rarity;
	}
	
	static MateriaRank ofTier(int tier) {
		for (int i = 0; i < values().length; i++) {
			MateriaRank rank = values()[i];
			if (tier == rank.tier) {
				return rank;
			}
		}
		return null;
	}
	
}
