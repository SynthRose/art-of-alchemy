package net.synthrose.artofalchemy.block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.synthrose.artofalchemy.essentia.Essentia;
import net.synthrose.artofalchemy.fluid.AoAFluids;

public class BlockEssentia extends FluidBlock {
	
	public static final Settings SETTINGS = Settings.copy(Blocks.WATER);
	protected static Essentia essentia;
	
	public BlockEssentia(Essentia essentia) {
		super(AoAFluids.ESSENTIA_FLUIDS.get(essentia), SETTINGS);
	}
	
	public Essentia getEssentia() {
		return essentia;
	}
	
	@Override
	public int getLuminance(BlockState state) {
		return 9;
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		entity.damage(DamageSource.MAGIC, 2);
//		world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_GENERIC_BURN,
//				entity.getSoundCategory(), 1.0F, 1.0F, false);
		world.addParticle(ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getY(), entity.getZ(),
				0.0D, 0.0D, 0.0D);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		double x = pos.getX() + random.nextDouble();
		double y = pos.getY() + random.nextDouble();
		double z = pos.getZ() + random.nextDouble();
		world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
		super.randomDisplayTick(state, world, pos, random);
	}

}
