package net.synthrose.artofalchemy.recipe;


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
import net.synthrose.artofalchemy.block.AoABlocks;
import net.synthrose.artofalchemy.essentia.EssentiaStack;

public class RecipeDissolution implements Recipe<Inventory> {
	
	protected Identifier id;
	protected String group;
	protected Ingredient input;
	protected EssentiaStack essentia;
	protected ItemStack container;
	
	public RecipeDissolution(Identifier id, String group, Ingredient input, EssentiaStack essentia, ItemStack container) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.essentia = essentia;
		this.container = container;
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
	
	public EssentiaStack getEssentia() {
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
	
	public ItemStack getContainer() {
		return container;
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
