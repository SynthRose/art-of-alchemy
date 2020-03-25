package io.github.synthrose.artofalchemy.essentia;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class Essentia {
	
	private int color;
	
	public Essentia(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
	
	public TranslatableText getName() {
		Identifier id = RegistryEssentia.INSTANCE.getId(this);
		return new TranslatableText("essentia." + id.getNamespace() + "." + id.getPath());
	}
	
}
