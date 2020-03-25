package io.github.synthrose.artofalchemy.recipe;

import com.google.gson.JsonObject;

import io.github.synthrose.artofalchemy.essentia.EssentiaStack;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class SerializerDissolution implements RecipeSerializer<RecipeDissolution> {

	@Override
	public RecipeDissolution read(Identifier id, JsonObject json) {
		String group = JsonHelper.getString(json, "group", "");
		Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
		EssentiaStack essentia = new EssentiaStack(JsonHelper.getObject(json, "result"));
		float factor = JsonHelper.getFloat(json, "factor", 1.0f);
		ItemStack container = ItemStack.EMPTY;
		if (json.has("container")) {
			container = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "container"));
		}
		return new RecipeDissolution(id, group, input, essentia, factor, container);
	}

	@Override
	public RecipeDissolution read(Identifier id, PacketByteBuf buf) {
		String group = buf.readString(32767);
		Ingredient input = Ingredient.fromPacket(buf);
		EssentiaStack essentia = new EssentiaStack(buf.readCompoundTag());
		float factor = buf.readFloat();
		ItemStack container = buf.readItemStack();
		return new RecipeDissolution(id, group, input, essentia, factor, container);
	}

	@Override
	public void write(PacketByteBuf buf, RecipeDissolution recipe) {
		buf.writeString(recipe.group);
		recipe.input.write(buf);
		buf.writeCompoundTag(recipe.essentia.toTag());
		buf.writeFloat(recipe.factor);
		buf.writeItemStack(recipe.container);
	}
	

}
