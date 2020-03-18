package net.synthrose.artofalchemy.recipe;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;

public class SerializerCalcination implements RecipeSerializer<RecipeCalcination> {

	@Override
	public RecipeCalcination read(Identifier id, JsonObject json) {
		String group = JsonHelper.getString(json, "group", "");
		Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
		ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
		int cost = JsonHelper.getInt(json, "cost", 1);
		ItemStack container = ItemStack.EMPTY;
		if (json.has("container")) {
			container = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "container"));
		}
		return new RecipeCalcination(id, group, input, output, cost, container);
	}

	@Override
	public RecipeCalcination read(Identifier id, PacketByteBuf buf) {
		String group = buf.readString(32767);
		Ingredient input = Ingredient.fromPacket(buf);
		ItemStack output = buf.readItemStack();
		int cost = buf.readVarInt();
		ItemStack container = buf.readItemStack();
		return new RecipeCalcination(id, group, input, output, cost, container);
	}

	@Override
	public void write(PacketByteBuf buf, RecipeCalcination recipe) {
		buf.writeString(recipe.group);
		recipe.input.write(buf);
		buf.writeItemStack(recipe.output);
		buf.writeVarInt(recipe.cost);
		buf.writeItemStack(recipe.container);
	}

}
