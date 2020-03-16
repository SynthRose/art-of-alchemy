package net.synthrose.artofalchemy.essentia;

import java.util.function.BiConsumer;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;
import net.synthrose.artofalchemy.ArtOfAlchemy;

public class RegistryEssentia extends SimpleRegistry<Essentia> {

	public static final RegistryEssentia INSTANCE = create(ArtOfAlchemy.id("essentia"));
	
	public void forEach(BiConsumer<Essentia, Identifier> function) {
		forEach((essentia) -> {
			function.accept(essentia, getId(essentia));
		});
	}
	
	private static RegistryEssentia create(Identifier id) {
		RegistryEssentia registry = new RegistryEssentia();
		return REGISTRIES.add(id, registry);
	}
	
}