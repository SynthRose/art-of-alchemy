package io.github.synthrose.artofalchemy.blockentity;

import java.util.HashSet;
import java.util.Set;

import io.github.synthrose.artofalchemy.block.BlockPipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import io.github.synthrose.artofalchemy.essentia.HasEssentia;

public class BlockEntityPipe extends BlockEntity implements Tickable {
	
	private int OPERATION_TIME = 10;
	private int timer = 0;

	public BlockEntityPipe() {
		super(AoABlockEntities.PIPE);
	}
	
	@Override
	public void tick() {
		if (timer % OPERATION_TIME != 0) {
			timer++;
			return;
		}
		if (world.isClient()) {
			return;
		}
		timer = 0;
		BlockState state = world.getBlockState(pos);
		Set<Direction> pullers = new HashSet<>();
		Set<Direction> pushers = new HashSet<>();
		Set<Direction> passives = new HashSet<>();
		if (state.getBlock() instanceof BlockPipe) {
			BlockPipe block = (BlockPipe) state.getBlock();
			
			for (Direction dir : Direction.values()) {
				switch (state.get(block.getProperty(dir))) {
				case PULL:
					pullers.add(dir);
					break;
				case PUSH:
					pushers.add(dir);
					break;
				case PASSIVE:
					passives.add(dir);
					break;
				default:
					break;
				}
			}
			for (Direction pushDir : pushers) {
				for (Direction pullDir : pullers) {
					transfer(pushDir, pullDir);
				}
				for (Direction passiveDir : passives) {
					transfer(pushDir, passiveDir);
				}
			}
			for (Direction pullDir : pullers) {
				for (Direction passiveDir : passives) {
					transfer(passiveDir, pullDir);
				}
			}
		}
	}
	
	public void transfer(Direction from, Direction to) {
		BlockEntity fromBE = world.getBlockEntity(pos.offset(from));
		BlockEntity toBE = world.getBlockEntity(pos.offset(to));
		if (fromBE instanceof HasEssentia && toBE instanceof HasEssentia) {
			for (int i = 0; i < ((HasEssentia) fromBE).getNumContainers(); i++) {
				for (int j = 0; j < ((HasEssentia) toBE).getNumContainers(); j++) {
					((HasEssentia) fromBE).getContainer(i).pushContents(((HasEssentia) toBE).getContainer(j));
				}
			}
			fromBE.markDirty();
			toBE.markDirty();
		}
	}

}
