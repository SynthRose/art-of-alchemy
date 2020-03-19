package net.synthrose.artofalchemy.recipe;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.synthrose.artofalchemy.essentia.EssentiaStack;

public class SerializerDissolution implements RecipeSerializer<RecipeDissolution> {

	@Override
	public RecipeDissolution read(Identifier id, JsonObject json) {
		String group = JsonHelper.getString(json, "group", "");
		Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
		EssentiaStack essentia = new EssentiaStack(JsonHelper.getObject(json, "result"));
		ItemStack container = ItemStack.EMPTY;
		if (json.has("container")) {
			container = ShapedRecipe.getItemStack(JsonHelper.getObject(json, "container"));
		}
		return new RecipeDissolution(id, group, input, essentia, container);
	}

	@Override
	public RecipeDissolution read(Identifier id, PacketByteBuf buf) {
		String group = buf.readString(32767);
		Ingredient input = Ingredient.fromPacket(buf);
		EssentiaStack essentia = new EssentiaStack(buf.readCompoundTag());
		ItemStack container = buf.readItemStack();
		return new RecipeDissolution(id, group, input, essentia, container);
	}

	@Override
	public void write(PacketByteBuf buf, RecipeDissolution recipe) {
		buf.writeString(recipe.group);
		recipe.input.write(buf);
		buf.writeCompoundTag(recipe.essentia.toTag());
		buf.writeItemStack(recipe.container);
	}
	

}
