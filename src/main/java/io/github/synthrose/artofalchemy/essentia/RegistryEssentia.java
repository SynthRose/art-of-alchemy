package io.github.synthrose.artofalchemy.essentia;

import java.util.function.BiConsumer;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.SimpleRegistry;

public class RegistryEssentia extends SimpleRegistry<Essentia> {

	public static final RegistryEssentia INSTANCE = create(ArtOfAlchemy.id("essentia"));
	
	public void forEach(BiConsumer<Essentia, Identifier> function) {
		for (Essentia essentia : this) {
			function.accept(essentia, getId(essentia));
		}
	}
	
	private static RegistryEssentia create(Identifier id) {
		RegistryEssentia registry = new RegistryEssentia();
		return REGISTRIES.add(id, registry);
	}
	
}