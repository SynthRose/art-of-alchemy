package io.github.synthrose.artofalchemy.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class SerializerProjection implements RecipeSerializer<RecipeProjection> {

	@Override
	public RecipeProjection read(Identifier id, JsonObject json) {
		String group = JsonHelper.getString(json, "group", "");
		Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
		int cost = JsonHelper.getInt(json, "cost", 1);
		ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "result"));
		int alkahest = JsonHelper.getInt(json, "alkahest", 0);
		return new RecipeProjection(id, group, input, cost, output, alkahest);
	}

	@Override
	public RecipeProjection read(Identifier id, PacketByteBuf buf) {
		String group = buf.readString(32767);
		Ingredient input = Ingredient.fromPacket(buf);
		int cost = buf.readVarInt();
		ItemStack output = buf.readItemStack();
		int alkahest = buf.readVarInt();
		return new RecipeProjection(id, group, input, cost, output, alkahest);
	}

	@Override
	public void write(PacketByteBuf buf, RecipeProjection recipe) {
		buf.writeString(recipe.group);
		recipe.input.write(buf);
		buf.writeVarInt(recipe.cost);
		buf.writeItemStack(recipe.output);
		buf.writeVarInt(recipe.alkahest);
	}

}
