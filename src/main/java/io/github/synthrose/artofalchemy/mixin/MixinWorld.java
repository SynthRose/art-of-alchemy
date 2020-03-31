package io.github.synthrose.artofalchemy.mixin;

import io.github.synthrose.artofalchemy.imixin.IMixinWorld;
import io.github.synthrose.artofalchemy.transport.EssentiaNetworker;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(World.class)
public abstract class MixinWorld implements IWorld, AutoCloseable, IMixinWorld {
    @Unique
    private final EssentiaNetworker essentiaNetworker = new EssentiaNetworker((World) (Object) this);

    @Unique
    public EssentiaNetworker getEssentiaNetworker() {
        return essentiaNetworker;
    }
}
