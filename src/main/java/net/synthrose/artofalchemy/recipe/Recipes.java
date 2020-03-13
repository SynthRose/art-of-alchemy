package net.synthrose.artofalchemy.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;

public class Recipes {
	
	public static RecipeType<RecipeCalcination> CALCINATION;
	public static RecipeSerializer<RecipeCalcination> CALCINATION_SERIALIZER;

	public static void registerRecipes() {
		CALCINATION = register("calcination");
		CALCINATION_SERIALIZER = register("calcination", new SerializerCalcination());
	}
	
	public static <T extends Recipe<?>> RecipeType<T> register(String name) {
		return Registry.register(Registry.RECIPE_TYPE, new Identifier(ArtOfAlchemy.MOD_ID, name),
			new RecipeType<T>() {
				public String toString() {
					return ArtOfAlchemy.MOD_ID + ":" + name;
				}
			});
	}
	
	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ArtOfAlchemy.MOD_ID, name), serializer);
	}

}
