package net.synthrose.artofalchemy.recipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.synthrose.artofalchemy.block.Blocks;

public class RecipeCalcination implements Recipe<Inventory> {
	
	protected Identifier id;
	protected String group;
	protected Ingredient input;
	protected ItemStack output;
	protected int cost;
	
	public RecipeCalcination(Identifier id, String group, Ingredient input, ItemStack output, int cost) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.cost = cost;
		this.output = output;
	}

	@Override
	public boolean matches(Inventory inv, World world) {
		return input.test(inv.getInvStack(0));
	}

	@Override
	public ItemStack craft(Inventory inv) {
		return output.copy();
	}
	
	public Ingredient getInput() {
		return input;
	}
	
	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public Identifier getId() {
		return id;
	}
	
	public int getCost() {
		return cost;
	}
	
	@Override
	public RecipeType<?> getType() {
		return Recipes.CALCINATION;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return Recipes.CALCINATION_SERIALIZER;
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
		return new ItemStack(Blocks.CALCINATOR);
	}

}
