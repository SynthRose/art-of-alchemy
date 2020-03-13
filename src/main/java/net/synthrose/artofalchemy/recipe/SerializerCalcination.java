package net.synthrose.artofalchemy.recipe;

import com.google.gson.JsonObject;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class SerializerCalcination implements RecipeSerializer<RecipeCalcination> {

	@Override
	public RecipeCalcination read(Identifier id, JsonObject json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecipeCalcination read(Identifier id, PacketByteBuf buf) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void write(PacketByteBuf buf, RecipeCalcination recipe) {
		// TODO Auto-generated method stub
		
	}

	

}
