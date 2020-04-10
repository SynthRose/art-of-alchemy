package io.github.synthrose.artofalchemy.recipe;


import io.github.synthrose.artofalchemy.block.AoABlocks;
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

public class RecipeProjection implements Recipe<Inventory> {

	protected final Identifier id;
	protected final String group;
	protected final Ingredient input;
	protected final int cost;
	protected final ItemStack output;
	protected final int alkahest;

	public RecipeProjection(Identifier id, String group, Ingredient input, int cost, ItemStack output, int alkahest) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.cost = cost;
		this.output = output;
		this.alkahest = alkahest;
	}

	@Override
	public boolean matches(Inventory inv, World world) {
		return input.test(inv.getStack(0));
	}

	@Override
	public ItemStack craft(Inventory inv) {
		return output.copy();
	}
	
	public Ingredient getInput() {
		return input;
	}

	public int getCost() {
		return cost;
	}
	
	@Override
	public ItemStack getOutput() {
		return output;
	}

	public int getAlkahest() {
		return alkahest;
	}

	@Override
	public Identifier getId() {
		return id;
	}
	
	@Override
	public RecipeType<?> getType() {
		return AoARecipes.PROJECTION;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return AoARecipes.PROJECTION_SERIALIZER;
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
		return new ItemStack(AoABlocks.PROJECTOR);
	}

}
