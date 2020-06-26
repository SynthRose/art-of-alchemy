package io.github.synthrose.artofalchemy.blockentity;

import io.github.synthrose.artofalchemy.AoAConfig;
import io.github.synthrose.artofalchemy.essentia.Essentia;
import io.github.synthrose.artofalchemy.essentia.EssentiaContainer;
import io.github.synthrose.artofalchemy.transport.HasEssentia;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

abstract public class AbstractBlockEntityCentrifuge extends BlockEntity implements HasEssentia, Tickable {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    protected EssentiaContainer input = new EssentiaContainer()
            .setCapacity(AoAConfig.get().centrifugeCapacity)
            .setInput(true)
            .setOutput(false);
    protected EssentiaContainer[] outputs;

    public AbstractBlockEntityCentrifuge(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public EssentiaContainer getContainer(int id) {
        if (id == 0) {
            return input;
        } else if (id > 0 && id <= getNumContainers()) {
            return outputs[id - 1];
        } else {
            return null;
        }
    }

    @Override
    public EssentiaContainer getContainer(Direction dir) {
        int horiz = dir.getHorizontal();
        if (horiz == -1) {
            return input;
        } else {
            horiz = (horiz - world.getBlockState(pos).get(FACING).getHorizontal() + 4) % 4;
            return outputs[horiz];
        }
    }

    @Override
    public int getNumContainers() {
        return outputs.length + 1;
    }

    @Override
    public void tick() {
        if (!input.isEmpty()) {
            for (EssentiaContainer output : outputs) {
                input.pushContents(output, true);
            }
        }
    }

    protected static EssentiaContainer outputOf(Essentia... essentia) {
        Set<Essentia> whitelist = new HashSet<>(Arrays.asList(essentia));
        return new EssentiaContainer()
                .setCapacity(AoAConfig.get().centrifugeCapacity)
                .setOutput(true)
                .setInput(false)
                .setWhitelist(whitelist)
                .setWhitelistEnabled(true);
    }
}
