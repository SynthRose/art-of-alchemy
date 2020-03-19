package net.synthrose.artofalchemy.recipe;

import com.google.gson.JsonObject;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.synthrose.artofalchemy.essentia.EssentiaStack;

public class SerializerSynthesis implements RecipeSerializer<RecipeSynthesis> {

	@Override
	public RecipeSynthesis read(Identifier id, JsonObject json) {
		String group = JsonHelper.getString(json, "group", "");
		Ingredient target = Ingredient.fromJson(JsonHelper.getObject(json, "target"));
		Ingredient materia = Ingredient.fromJson(JsonHelper.getObject(json, "materia"));
		EssentiaStack essentia = new EssentiaStack(JsonHelper.getObject(json, "essentia"));
		Ingredient container = Ingredient.EMPTY;
		if (json.has("container")) {
			container = Ingredient.fromJson(JsonHelper.getObject(json, "container"));
		}
		int cost = JsonHelper.getInt(json, "cost", 1);
		return new RecipeSynthesis(id, group, target, materia, essentia, container, cost);
	}

	@Override
	public RecipeSynthesis read(Identifier id, PacketByteBuf buf) {
		String group = buf.readString(32767);
		Ingredient target = Ingredient.fromPacket(buf);
		Ingredient materia = Ingredient.fromPacket(buf);
		EssentiaStack essentia = new EssentiaStack(buf.readCompoundTag());
		Ingredient container = Ingredient.fromPacket(buf);
		int cost = buf.readVarInt();
		return new RecipeSynthesis(id, group, target, materia, essentia, container, cost);
	}

	@Override
	public void write(PacketByteBuf buf, RecipeSynthesis recipe) {
		buf.writeString(recipe.group);
		recipe.target.write(buf);
		recipe.materia.write(buf);
		buf.writeCompoundTag(recipe.essentia.toTag());
		recipe.container.write(buf);
		buf.writeVarInt(recipe.cost);
	}
	

}
