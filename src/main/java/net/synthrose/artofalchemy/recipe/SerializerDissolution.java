package net.synthrose.artofalchemy.recipe;

import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.synthrose.artofalchemy.essentia.EssentiaStack;

public class SerializerDissolution implements RecipeSerializer<RecipeDissolution> {

	@Override
	public RecipeDissolution read(Identifier id, JsonObject json) {
		String group = JsonHelper.getString(json, "group", "");
		Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
		JsonObject essentiaObj = JsonHelper.getObject(json, "result");
		EssentiaStack essentia = new EssentiaStack(essentiaObj);
		return new RecipeDissolution(id, group, input, essentia);
	}

	@Override
	public RecipeDissolution read(Identifier id, PacketByteBuf buf) {
		String group = buf.readString(32767);
		Ingredient input = Ingredient.fromPacket(buf);
		CompoundTag essentiaTag = buf.readCompoundTag();
		EssentiaStack essentia = new EssentiaStack(essentiaTag);
		return new RecipeDissolution(id, group, input, essentia);
	}

	@Override
	public void write(PacketByteBuf buf, RecipeDissolution recipe) {
		buf.writeString(recipe.group);
		recipe.input.write(buf);
		buf.writeCompoundTag(recipe.essentia.toTag());
	}
	

}
