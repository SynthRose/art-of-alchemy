package net.synthrose.artofalchemy;

public enum EssentiaType {
	MERCURY	("mercury",	0x9291CC),
	VENUS	("venus",	0xEF91CF),
	TELLUS	("tellus",	0x20761C),
	MARS	("mars",	0xB00A09),
	JUPITER	("jupiter",	0xE98765),
	SATURN	("saturn",	0xD2D88C),
	URANUS	("uranus",	0x00B3AD),
	NEPTUNE	("neptune",	0x283B78),
	APOLLO	("apollo",	0xDCB500),
	DIANA	("diana",	0x5BB0F0),
	CERES	("ceres",	0x5D3E22),
	PLUTO	("pluto",	0x185665),
	VOID	("void",	0x5E1CD9);
	
	private String name;
	private int color;
	
	private EssentiaType(String name, int color) {
		this.name = name;
		this.color = color;
	}
	
	private EssentiaType(String name) {
		this.name = name;
		this.color = 0xFFFFFF;
	}
	
	public String getName() {
		return name;
	}
	
	public int getColor() {
		return color;
	}
}
