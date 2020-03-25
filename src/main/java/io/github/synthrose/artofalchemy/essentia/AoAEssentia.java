package io.github.synthrose.artofalchemy.essentia;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import net.minecraft.util.registry.Registry;

public class AoAEssentia {
	
	public static final Essentia MERCURY = new Essentia(0x9291CC);
	public static final Essentia VENUS = new Essentia(0xEF91CF);
	public static final Essentia TELLUS = new Essentia(0x20761C);
	public static final Essentia MARS = new Essentia(0xB00A09);
	public static final Essentia JUPITER = new Essentia(0xE98765);
	public static final Essentia SATURN = new Essentia(0xD2D88C);
	public static final Essentia URANUS = new Essentia(0x00B3AD);
	public static final Essentia NEPTUNE = new Essentia(0x283B78);
	public static final Essentia APOLLO = new Essentia(0xDCB500);
	public static final Essentia DIANA = new Essentia(0x5BB0F0);
	public static final Essentia CERES = new Essentia(0x5D3E22);
	public static final Essentia PLUTO = new Essentia(0x185665);
	public static final Essentia VOID = new Essentia(0x5E1CD9);
	
	public static void registerEssentia() {
		register("mercury", MERCURY);
		register("venus", VENUS);
		register("tellus", TELLUS);
		register("mars", MARS);
		register("jupiter", JUPITER);
		register("saturn", SATURN);
		register("uranus", URANUS);
		register("neptune", NEPTUNE);
		register("apollo", APOLLO);
		register("diana", DIANA);
		register("ceres", CERES);
		register("pluto", PLUTO);
		register("void", VOID);
	}
	
	public static Essentia register(String name, Essentia essentia) {
		return Registry.register(RegistryEssentia.INSTANCE, ArtOfAlchemy.id(name), essentia);
	}
	
}
