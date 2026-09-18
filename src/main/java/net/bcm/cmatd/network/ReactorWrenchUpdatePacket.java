package net.bcm.cmatd.network;

import net.bcm.cmatd.blockentity.FoodReactorMultiblock;
import net.bcm.cmatd.blockentity.RadioactiveReactor;
import net.bcm.cmatd.datagen.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ReactorWrenchUpdatePacket {
    public static final ReactorWrenchUpdatePacket INSTANCE = new ReactorWrenchUpdatePacket();

    public static ReactorWrenchUpdatePacket get(){
        return INSTANCE;
    }

    public void handle(ReactorWrenchUpdate message, IPayloadContext ctx) {
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
            else if(level.getBlockEntity(message.pos()) instanceof RadioactiveReactor reactor){
                if(reactor.isFormed){ // reactor is formed
                    if(!reactor.sealedCore){ // must be formed in order to seal it
                        reactor.showNumSealingBlockPosCorrect(); // show the needed sealant positions
                    }
                }
                else{
                    reactor.showNumBlockPosCorrect(); // show the needed forming positions
                }
            }
        }
    }
}
