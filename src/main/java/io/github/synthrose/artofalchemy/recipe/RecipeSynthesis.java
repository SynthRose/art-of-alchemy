package io.github.synthrose.artofalchemy.recipe;


import io.github.synthrose.artofalchemy.AoAHelper;
import io.github.synthrose.artofalchemy.block.AoABlocks;
import io.github.synthrose.artofalchemy.essentia.EssentiaStack;
import io.github.synthrose.artofalchemy.item.ItemAlchemyFormula;
import io.github.synthrose.artofalchemy.item.ItemJournal;
import io.github.synthrose.artofalchemy.item.ItemMateria;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RecipeSynthesis implements Recipe<Inventory> {
	
	protected final Identifier id;
	protected final String group;
	protected final Ingredient target;
	protected final Ingredient materia;
	protected final EssentiaStack essentia;
	protected final Ingredient container;
	protected final int cost;
	
	public RecipeSynthesis(Identifier id, String group, Ingredient target, Ingredient materia, EssentiaStack essentia,
			Ingredient container, int cost) {
		this.id = id;
		this.group = group;
		this.target = target;
		this.materia = materia;
		this.essentia = essentia;
		this.container = container;
		this.cost = cost;
	}

	@Override
	public boolean matches(Inventory inv, World world) {
		ItemStack stack = inv.getInvStack(2);
		return target.test(new ItemStack(AoAHelper.getTarget(stack)));
	}

	@Override
	public ItemStack craft(Inventory inv) {
		return ItemStack.EMPTY;
	}
	
	@Override
	public Identifier getId() {
		return id;
	}
	
	public Ingredient getMateria() {
		return materia;
	}
	
	public EssentiaStack getEssentia() {
		return (EssentiaStack) essentia.clone();
	}
	
	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}
	
	public Ingredient getContainer() {
		return container;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getXp(ItemStack input) {
		int tier = 1;
		if (input.getItem() instanceof ItemMateria) {
			tier = ((ItemMateria) input.getItem()).getTier() + 1;
		}
		return Math.max(1, cost * tier + (essentia.getCount() / 48));
	}
	
	@Override
	public RecipeType<?> getType() {
		return AoARecipes.SYNTHESIS;
	}
	
	@Override
	public RecipeSerializer<?> getSerializer() {
		return AoARecipes.SYNTHESIS_SERIALIZER;
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
		return new ItemStack(AoABlocks.SYNTHESIZER);
	}

}
