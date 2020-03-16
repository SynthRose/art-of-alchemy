package net.synthrose.artofalchemy.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;
import net.synthrose.artofalchemy.ArtOfAlchemy;

public class AoARecipes {
	
	public static RecipeType<RecipeCalcination> CALCINATION;
	public static RecipeSerializer<RecipeCalcination> CALCINATION_SERIALIZER;
	
	public static RecipeType<RecipeDissolution> DISSOLUTION;
	public static RecipeSerializer<RecipeDissolution> DISSOLUTION_SERIALIZER;

	public static void registerRecipes() {
		CALCINATION = register("calcination");
		CALCINATION_SERIALIZER = register("calcination", new SerializerCalcination());
		
		DISSOLUTION = register("dissolution");
		DISSOLUTION_SERIALIZER = register("dissolution", new SerializerDissolution());
	}
	
	public static <T extends Recipe<?>> RecipeType<T> register(String name) {
		return Registry.register(Registry.RECIPE_TYPE, ArtOfAlchemy.id(name),
			new RecipeType<T>() {
				public String toString() {
					return ArtOfAlchemy.MOD_ID + ":" + name;
				}
			});
	}
	
	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, ArtOfAlchemy.id(name), serializer);
	}

}
