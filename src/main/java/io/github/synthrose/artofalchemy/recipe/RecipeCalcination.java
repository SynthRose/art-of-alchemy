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

public class RecipeCalcination implements Recipe<Inventory> {
	
	protected final Identifier id;
	protected final String group;
	protected final Ingredient input;
	protected final ItemStack output;
	protected final float factor;
	protected final ItemStack container;
	
	public RecipeCalcination(Identifier id, String group, Ingredient input, ItemStack output, float factor, ItemStack container) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.factor = factor;
		this.output = output;
		this.container = container;
	}

	public RecipeCalcination(Identifier id, String group, Ingredient input, ItemStack output, ItemStack container) {
		this(id, group, input, output, 1.0f, container);
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
	
	public float getFactor() {
		return factor;
	}
	
	public ItemStack getContainer() {
		return container;
	}
	
	@Override
	public RecipeType<?> getType() {
		return AoARecipes.CALCINATION;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return AoARecipes.CALCINATION_SERIALIZER;
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
		return new ItemStack(AoABlocks.CALCINATOR);
	}

}
