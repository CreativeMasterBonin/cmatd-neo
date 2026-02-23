package net.bcm.cmatd;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

@SuppressWarnings({"unused","deprecated"})
public class ServerUtilities {
    public static void generateServerParticles(Level level, ParticleOptions type, BlockPos pos,
                                               int count, BlockPos offsetPos, int speed){
        if(level instanceof ServerLevel){
            ((ServerLevel) level).sendParticles(type,
                    pos.getX(),pos.getY(),pos.getZ(),
                    count, offsetPos.getX(),offsetPos.getY(),offsetPos.getZ(),
                    speed);
        }
    }

    public static void generateServerParticlesRandomInBlockPos(Level level, ParticleOptions type, BlockPos pos,
                                               int count, BlockPos offsetPos, int speed, int range){
        if(level instanceof ServerLevel){
            int offset = level.getRandom().nextIntBetweenInclusive(-1,1);
            for(int i = 0; i < count; i++){
                ((ServerLevel) level).sendParticles(type,
                        pos.getX() - 1,pos.getY() - 1,pos.getZ() - 1,
                        1, offsetPos.getX() + offset,offsetPos.getY() + offset,offsetPos.getZ() + offset,
                        speed);
            }
        }
    }
}
