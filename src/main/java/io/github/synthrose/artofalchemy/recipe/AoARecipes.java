package io.github.synthrose.artofalchemy.recipe;

import io.github.synthrose.artofalchemy.ArtOfAlchemy;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public class AoARecipes {
	
	public static RecipeType<RecipeCalcination> CALCINATION;
	public static RecipeSerializer<RecipeCalcination> CALCINATION_SERIALIZER;
	
	public static RecipeType<RecipeDissolution> DISSOLUTION;
	public static RecipeSerializer<RecipeDissolution> DISSOLUTION_SERIALIZER;
	
	public static RecipeType<RecipeSynthesis> SYNTHESIS;
	public static RecipeSerializer<RecipeSynthesis> SYNTHESIS_SERIALIZER;

	public static RecipeType<RecipeProjection> PROJECTION;
	public static RecipeSerializer<RecipeProjection> PROJECTION_SERIALIZER;

	public static void registerRecipes() {
		CALCINATION = register("calcination");
		CALCINATION_SERIALIZER = register("calcination", new SerializerCalcination());
		
		DISSOLUTION = register("dissolution");
		DISSOLUTION_SERIALIZER = register("dissolution", new SerializerDissolution());
		
		SYNTHESIS = register("synthesis");
		SYNTHESIS_SERIALIZER = register("synthesis", new SerializerSynthesis());

		PROJECTION = register("projection");
		PROJECTION_SERIALIZER = register("projection", new SerializerProjection());
	}
	
	public static <T extends Recipe<?>> RecipeType<T> register(String name) {
		return Registry.register(Registry.RECIPE_TYPE, ArtOfAlchemy.id(name),
			new RecipeType<T>() {
				public String toString() {
					return ArtOfAlchemy.id(name).toString();
				}
			});
	}
	
	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, ArtOfAlchemy.id(name), serializer);
	}

}
