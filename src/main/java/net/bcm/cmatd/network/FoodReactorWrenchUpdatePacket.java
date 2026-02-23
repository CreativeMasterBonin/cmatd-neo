package net.bcm.cmatd.network;

import net.bcm.cmatd.blockentity.FoodReactorMultiblock;
import net.bcm.cmatd.datagen.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class FoodReactorWrenchUpdatePacket{
    public static final FoodReactorWrenchUpdatePacket INSTANCE = new FoodReactorWrenchUpdatePacket();

    public static FoodReactorWrenchUpdatePacket get(){
        return INSTANCE;
    }

    public void handle(FoodReactorWrenchUpdate message, IPayloadContext ctx) {
        var level = ctx.player().level();
        Player player = ctx.player();

        if (level.isLoaded(message.pos())) {
            if (level.getBlockEntity(message.pos()) instanceof FoodReactorMultiblock be) {
                be.checkMultiblockForm();
                be.setChanged();
                if(!be.onlyCheckMultiblockFormNoUpdate()){
                    player.displayClientMessage(Component
                                    .translatable("message.multiblock.unformed_with_number_type",
                                            be.tempBlocksToBeReplaced,
                                            Tag.VALID_FOOD_REACTOR_CASINGS.location().toString()),
                            true);
                }
                else{
                    player.displayClientMessage(Component
                                    .translatable("message.multiblock.formed_successfully",
                                            message.pos().toShortString()),
                            true);
                }
            }
        }
    }
}
