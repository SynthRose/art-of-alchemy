package io.github.synthrose.artofalchemy.mixin;

import io.github.synthrose.artofalchemy.recipe.AoARecipes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientRecipeBook.class)
public abstract class MixinClientRecipeBook {
    @Inject(method = "getGroupForRecipe", at = @At(value = "HEAD"), cancellable = true)
    private static void getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        RecipeType<?> type = recipe.getType();
        if (type == AoARecipes.SYNTHESIS || type == AoARecipes.CALCINATION || type == AoARecipes.DISSOLUTION ||
                type == AoARecipes.PROJECTION) {
            cir.setReturnValue(RecipeBookGroup.UNKNOWN);
        }
    }
}
