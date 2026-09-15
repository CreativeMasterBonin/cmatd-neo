package net.bcm.cmatd.block.custom;

import net.bcm.cmatd.blockentity.RadioactiveReactor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class RadioactiveReactorBlock extends TieredMachineBlock{
    public RadioactiveReactorBlock(Properties properties) {
        super(properties.strength(1f,50f)
                .sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops()
                .mapColor(MapColor.COLOR_GRAY).pushReaction(PushReaction.BLOCK)
                .instrument(NoteBlockInstrument.BANJO));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return InteractionResult.PASS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RadioactiveReactor(blockPos,blockState);
    }
}
