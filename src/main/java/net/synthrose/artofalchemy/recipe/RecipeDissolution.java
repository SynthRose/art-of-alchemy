package net.synthrose.artofalchemy.recipe;

import java.util.Map;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.synthrose.artofalchemy.EssentiaType;
import net.synthrose.artofalchemy.block.AoABlocks;

public class RecipeDissolution implements Recipe<Inventory> {
	
	protected Identifier id;
	protected String group;
	protected Ingredient input;
	protected Map<EssentiaType, Integer> essentia;
	
	public RecipeDissolution(Identifier id, String group, Ingredient input,
			Map<EssentiaType, Integer> essentia) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.essentia = essentia;
	}

	@Override
	public boolean matches(Inventory inv, World world) {
		return input.test(inv.getInvStack(0));
	}

	@Override
	public ItemStack craft(Inventory inv) {
		return ItemStack.EMPTY;
	}
	
	public Ingredient getInput() {
		return input;
	}
	
	public Map<EssentiaType, Integer> getEssentia() {
		return essentia;
	}
	
	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public Identifier getId() {
		return id;
	}
	
	@Override
	public RecipeType<?> getType() {
		return AoARecipes.DISSOLUTION;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return AoARecipes.DISSOLUTION_SERIALIZER;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public boolean fits(int width, int height) {
		return true;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public String getGroup() {
		return group;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(AoABlocks.DISSOLVER);
	}

}
