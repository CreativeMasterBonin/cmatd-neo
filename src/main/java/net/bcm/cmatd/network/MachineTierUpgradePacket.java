package net.bcm.cmatd.network;

import net.bcm.cmatd.blockentity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MachineTierUpgradePacket{
    public static final MachineTierUpgradePacket INSTANCE = new MachineTierUpgradePacket();

    public static MachineTierUpgradePacket get(){
        return INSTANCE;
    }

    public void handle(final MachineTierUpgradePayload payload, final IPayloadContext ctx){
        ctx.enqueueWork(() -> {
            Player ply = ctx.player();
            if(ply == null){
                return;
            }
            Level level = ctx.player().level();
            if(!level.hasChunkAt(payload.position())){
                return;
            }

            // change machine tier and update night mode if the night mode tier was never unlocked
            if(level.getBlockEntity(payload.position()) instanceof JamMakerBE jamMakerBE){
                if(!jamMakerBE.nightUpgrade){
                    jamMakerBE.nightUpgrade = true;
                }
                jamMakerBE.machineTier = payload.tierToUpgradeTo();
                jamMakerBE.updateBlock();
                return;
            }
            else if(level.getBlockEntity(payload.position()) instanceof PresserBE presserBE) {
                if (!presserBE.nightUpgrade) {
                    presserBE.nightUpgrade = true;
                }
                presserBE.machineTier = payload.tierToUpgradeTo();
                presserBE.updateBlock();
                return;
            }
            else if(level.getBlockEntity(payload.position()) instanceof BaseEnergyMakerBE energyMakerBE){
                if(energyMakerBE.machine_tier < payload.tierToUpgradeTo()){
                    energyMakerBE.upgradeToTier(payload.tierToUpgradeTo());
                    energyMakerBE.setChanged();
                    return;
                }
            }
            else if(level.getBlockEntity(payload.position()) instanceof FoodReactorMultiblock foodReactor){
                if(foodReactor.machine_tier < payload.tierToUpgradeTo()){
                    foodReactor.machine_tier = payload.tierToUpgradeTo();
                    foodReactor.setChanged();
                    return;
                }
            }
            // for any machines specifically capable of mass upgrading tiers
            else if(level.getBlockEntity(payload.position()) instanceof TieredMachine tieredMachine){
                if(!(tieredMachine.machineTier >= payload.tierToUpgradeTo())){
                    tieredMachine.machineTier = payload.tierToUpgradeTo();
                    tieredMachine.updateBlock();
                }
                return;
            }
        });
    }
}
