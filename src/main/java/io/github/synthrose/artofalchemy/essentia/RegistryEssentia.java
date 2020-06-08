package io.github.synthrose.artofalchemy.essentia;

import com.mojang.serialization.Lifecycle;
import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import io.github.synthrose.artofalchemy.mixin.RegistryAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;

import java.util.function.BiConsumer;

public class RegistryEssentia extends SimpleRegistry<Essentia> {

	public static final RegistryKey<Registry<Essentia>> KEY = RegistryKey.ofRegistry(ArtOfAlchemy.id("essentia"));
	public static final RegistryEssentia INSTANCE = RegistryAccessor.create(KEY, new RegistryEssentia(), null);

	public RegistryEssentia() {
		super(KEY, Lifecycle.stable());
	}

	public void forEach(BiConsumer<Essentia, Identifier> function) {
		for (Essentia essentia : this) {
			function.accept(essentia, getId(essentia));
		}
	}

}