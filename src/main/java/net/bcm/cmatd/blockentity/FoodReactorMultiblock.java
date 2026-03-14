package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.Cmatd;
import net.bcm.cmatd.Components;
import net.bcm.cmatd.Utility;
import net.bcm.cmatd.block.CmatdBlock;
import net.bcm.cmatd.datagen.FoodReactorFuels;
import net.bcm.cmatd.datagen.Tag;
import net.bcm.cmatd.item.CmatdItem;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import static net.bcm.cmatd.Cmatd.FOOD_REACTOR_FLUID_HANDLER;
import static net.bcm.cmatd.Cmatd.FOOD_REACTOR_FUELS;

public class FoodReactorMultiblock extends BlockEntity {
    /*
    0 -> fuel input (mashed potatoes)
    1 -> coolant input (jams)

    2 -> waste output (some yucky item)

    3 -> module slot 1
    4 -> module slot 2
    5 -> module slot 3

    0-fluid -> leftover coolant output (water)
    */
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(6);
    public final BaseEnergyStorage energyStorage = new BaseEnergyStorage(1000000,50000,50000);

    public int ticks;
    public boolean multiblockFormed = false;
    public int progress;
    private final int ticksToWaitOperation = 10;

    public final FluidContainerData fluidContainer;

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }
    public ItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }
    public FluidTank getFluidTank() {
        return getData(FOOD_REACTOR_FLUID_HANDLER.get());
    }

    public FluidContainerData getFluidContainer() {
        return fluidContainer;
    }

    public FoodReactorMultiblock(BlockPos pos, BlockState blockState) {
        super(CmatdBE.FOOD_REACTOR.get(), pos, blockState);
        this.fluidContainer = new FluidContainerData(this);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void serverTick(){
        ticks++;
        if(ticks > 32767){
            ticks = 0;
        }
        // check if multiblock is formed
        if(multiblockFormed){
            ItemStack module1 = itemStackHandler.getStackInSlot(3);
            ItemStack module2 = itemStackHandler.getStackInSlot(4);
            ItemStack module3 = itemStackHandler.getStackInSlot(5);
            if(ticks % 80 == 0){
                checkMultiblockForm();
            }
            doStuff(module1,module2,module3);
        }
    }

    // 0 = input coolant, 1 = input fuel, 2 = output waste, 3 = module 1, 4 = module 2, 5 = module 3
    private void doStuff(ItemStack module1, ItemStack module2, ItemStack module3){
        int speed_modules = 0;
        if(!module1.isEmpty()){
            if(module1.get(Components.MODULE_TYPE) == 0){
                speed_modules += (1 + module1.getCount());
            }
        }
        if(!module2.isEmpty()){
            if(module2.get(Components.MODULE_TYPE) == 0){
                speed_modules += (1 + module2.getCount());
            }
        }
        if(!module3.isEmpty()){
            if(module3.get(Components.MODULE_TYPE) == 0){
                speed_modules += (1 + module3.getCount());
            }
        }

        if(speed_modules < 0){
            speed_modules = 0;
        }
        else if(speed_modules > ticksToWaitOperation){
            speed_modules = ticksToWaitOperation - 1;
        }

        int preCheckNum = ticksToWaitOperation - speed_modules;
        if(preCheckNum <= 0){
            preCheckNum = 1;
        }

        //
        if(ticks % (preCheckNum) == 0){
            ItemStack coolant = itemStackHandler.getStackInSlot(0);
            ItemStack fuel = itemStackHandler.getStackInSlot(1);
            ItemStack waste = itemStackHandler.getStackInSlot(2);
            FluidStack outputFluid = getFluidTank().getFluid();
            boolean canProcessIsWater = false;
            boolean canProcessLiquidEmpty = false;

            boolean doubleOutput = false;
            boolean tripleOutput = false;

            int efficiency_modules = 0;

            if(!module1.isEmpty()){
                if(module1.has(Components.MODULE_TYPE)){
                    if(module1.get(Components.MODULE_TYPE) == 1){
                        efficiency_modules += (1 + module1.getCount());
                    }
                    else if(module1.get(Components.MODULE_TYPE) == 2){
                        doubleOutput = true;
                    }
                    else if(module1.get(Components.MODULE_TYPE) == 3){
                        tripleOutput = true;
                    }
                }
            }
            if(!module2.isEmpty()){
                if(module2.has(Components.MODULE_TYPE)){
                    if(module2.get(Components.MODULE_TYPE) == 1){
                        efficiency_modules += (1 + module2.getCount());
                    }
                    else if(module2.get(Components.MODULE_TYPE) == 2){
                        doubleOutput = true;
                    }
                    else if(module2.get(Components.MODULE_TYPE) == 3){
                        tripleOutput = true;
                    }
                }
            }
            if(!module3.isEmpty()){
                if(module3.has(Components.MODULE_TYPE)){
                    if(module3.get(Components.MODULE_TYPE) == 1){
                        efficiency_modules += (1 + module3.getCount());
                    }
                    else if(module3.get(Components.MODULE_TYPE) == 2){
                        doubleOutput = true;
                    }
                    else if(module3.get(Components.MODULE_TYPE) == 3){
                        tripleOutput = true;
                    }
                }
            }

            if(efficiency_modules < 0){
                efficiency_modules = 0;
            }
            else if(efficiency_modules > 16){
                efficiency_modules = 16;
            }

            if(waste.getCount() == waste.getMaxStackSize()){
                return;
            }
            if(outputFluid.is(Fluids.WATER) && outputFluid.getAmount() == Utility.FOOD_REACTOR_FLUID_CAPACITY){
                return;
            }

            progress += 1 + (1 * efficiency_modules);
            if(progress > 32){
                progress = 32;
            }

            Holder<Item> itemHolder = fuel.getItemHolder();
            FoodReactorFuels foodReactorFuels = itemHolder.getData(FOOD_REACTOR_FUELS);

            boolean oldValidateCheck = coolant.is(Tag.VALID_FOOD_REACTOR_COOLANTS) && fuel.is(Tag.VALID_FOOD_REACTOR_FUELS);
            boolean validateCheck = coolant.is(Tag.VALID_FOOD_REACTOR_COOLANTS) && foodReactorFuels != null;

            // support datamap-driven fuels
            if(validateCheck && progress >= 32){
                int waterAmount = foodReactorFuels.waterAmountOutput();
                float randomPowerMultiplier = foodReactorFuels.powerOutputMultiplier().sample(level.getRandom());

                float normalEnergyMultipliedCheckNoOverflow = 1000.0f * (float)randomPowerMultiplier; // the multiplied output
                int outputEnergy = 1000; // the checked output energy that is clamped

                if(normalEnergyMultipliedCheckNoOverflow < 0.000f){
                    normalEnergyMultipliedCheckNoOverflow = 1.000f;
                }

                outputEnergy = Mth.clamp(Math.round(normalEnergyMultipliedCheckNoOverflow),1000,energyStorage.getCapacity());

                // old values
                int waterNormal = 100;
                int waterDoubled = 200;
                int waterTripled = 300;

                if(outputFluid.isEmpty()){
                    canProcessLiquidEmpty = true;
                }
                else{
                    if(outputFluid.is(Tags.Fluids.WATER)){
                        canProcessIsWater = true;
                    }
                    else{
                        return; // CAN'T STORE ANY OTHER LIQUIDS OR FAILURE
                    }
                }
                // if water or empty
                if(outputFluid.isEmpty()){
                    if(doubleOutput){
                        getFluidTank().fill(new FluidStack(Fluids.WATER,waterAmount * 2),IFluidHandler.FluidAction.EXECUTE);
                    }
                    else if(tripleOutput){
                        getFluidTank().fill(new FluidStack(Fluids.WATER,waterAmount * 3),IFluidHandler.FluidAction.EXECUTE);
                    }
                    else{
                        getFluidTank().fill(new FluidStack(Fluids.WATER,waterAmount),IFluidHandler.FluidAction.EXECUTE);
                    }
                }
                else{
                    if(outputFluid.is(Tags.Fluids.WATER)){
                        if(doubleOutput){
                            getFluidTank().fill(new FluidStack(getFluidTank().getFluid().getFluid(),waterAmount * 2),IFluidHandler.FluidAction.EXECUTE);
                        }
                        else if(tripleOutput){
                            getFluidTank().fill(new FluidStack(getFluidTank().getFluid().getFluid(),waterAmount * 3),IFluidHandler.FluidAction.EXECUTE);
                        }
                        else{
                            getFluidTank().fill(new FluidStack(getFluidTank().getFluid().getFluid(),waterAmount),IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }

                int multiplier2 = doubleOutput ? 2 : 1;
                int multiplier3 = tripleOutput ? 3 : 1;

                if(waste.isEmpty()){
                    coolant.shrink(1);
                    fuel.shrink(1);
                    itemStackHandler.setStackInSlot(2,new ItemStack(CmatdItem.JAM_JAR.asItem()));
                    level.playSound(null,getBlockPos(),
                            SoundEvents.BOAT_PADDLE_WATER, SoundSource.BLOCKS,0.75f,1.0f);
                    energyStorage.receiveEnergy(outputEnergy * (multiplier2 + multiplier3),false);
                }
                else{
                    if(waste.getCount() < waste.getMaxStackSize()){
                        coolant.shrink(1);
                        fuel.shrink(1);
                        waste.grow(1);
                        level.playSound(null,getBlockPos(),
                                SoundEvents.BOAT_PADDLE_WATER, SoundSource.BLOCKS,0.75f,1.0f);
                        energyStorage.receiveEnergy(outputEnergy * (multiplier2 + multiplier3),false);
                    }
                }
                progress = 0;
            }
            setChanged();
        }
    }

    @SuppressWarnings("unused,deprecated")
    private void debugMessage(){
        if(SharedConstants.IS_RUNNING_IN_IDE)
            Cmatd.getLogger().debug("FORMED: {}. ENERGY: {}. FLUID: {}.",multiblockFormed,energyStorage.getEnergyStored(),
                getFluidTank().getFluid());
    }

    public void checkMultiblockForm(){
        int validBlockCount = 0;
        int blocksToBeReplaced = 0;
        for(int x = getBlockPos().getX() - 3; x < getBlockPos().getX() + 1; x++){
            for(int y = getBlockPos().getY() - 1; y < getBlockPos().getY() + 2; y++){
                for(int z = getBlockPos().getZ() - 3; z < getBlockPos().getZ() + 1; z++){
                    boolean isIron = level.getBlockState(new BlockPos(x,y,z)).is(Tag.VALID_FOOD_REACTOR_CASINGS);
                    if(isIron){
                        validBlockCount++;
                        if(level.getBlockState(new BlockPos(x,y,z)).is(CmatdBlock.FOOD_REACTOR_MULTIBLOCK)){
                            validBlockCount--;
                        }
                    }
                    else{
                        if(!level.getBlockState(new BlockPos(x,y,z)).is(CmatdBlock.FOOD_REACTOR_MULTIBLOCK)){
                            blocksToBeReplaced++;
                            if(level instanceof ServerLevel){
                                ((ServerLevel) level).sendParticles(ParticleTypes.DRIPPING_LAVA,
                                        x + 0.5,y + 0.5,z + 0.5,
                                        1,0,0,0,0);
                            }
                        }
                    }
                }
            }
        }
        // all blocks minus the controller
        if(validBlockCount == 47 && blocksToBeReplaced == 0) {
            if(!multiblockFormed){
                multiblockFormed = true;
            }
            setChanged();
        }
        else{
            multiblockFormed = false;
            setChanged();
        }
    }

    public int tempValidBlockCount = 0;
    public int tempBlocksToBeReplaced = 0;

    public boolean onlyCheckMultiblockFormNoUpdate(){
        int validBlockCount = 0;
        int blocksToBeReplaced = 0;
        for(int x = getBlockPos().getX() - 3; x < getBlockPos().getX() + 1; x++){
            for(int y = getBlockPos().getY() - 1; y < getBlockPos().getY() + 2; y++){
                for(int z = getBlockPos().getZ() - 3; z < getBlockPos().getZ() + 1; z++){
                    boolean isIron = level.getBlockState(new BlockPos(x,y,z)).is(Tag.VALID_FOOD_REACTOR_CASINGS);
                    if(isIron){
                        validBlockCount++;
                        if(level.getBlockState(new BlockPos(x,y,z)).is(CmatdBlock.FOOD_REACTOR_MULTIBLOCK)){
                            validBlockCount--;
                        }
                    }
                    else{
                        if(!level.getBlockState(new BlockPos(x,y,z)).is(CmatdBlock.FOOD_REACTOR_MULTIBLOCK)){
                            blocksToBeReplaced++;
                        }
                    }
                }
            }
        }
        tempValidBlockCount = validBlockCount;
        tempBlocksToBeReplaced = blocksToBeReplaced;
        // all blocks minus the controller
        if(validBlockCount == 47 && blocksToBeReplaced == 0) {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(6,ItemStack.EMPTY);

        for(int f1 = 0; f1 < 6; f1++){
            if(f1 >= 7){
                break;
            }
            else{
                itemStacks.set(f1,itemStackHandler.getStackInSlot(f1));
            }
        }
        ContainerHelper.saveAllItems(tag,itemStacks,registries);

        tag.putInt("energy",energyStorage.getEnergyStored());

        FluidStack fluid = this.getFluidTank().getFluid();
        int fluidAmount = this.getFluidTank().getFluidAmount();

        tag.putInt("ticks",ticks);
        tag.putBoolean("multiblock_formed",multiblockFormed);
        tag.putInt("progress",progress);

        tag.putInt("fluid_amount",fluidAmount);
        tag.putString("fluid",fluid.getFluid().toString());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(6,ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag,itemStacks,registries);

        for(int f1 = 0; f1 < 6; f1++){
            if(f1 >= 7){
                break;
            }
            else{
                itemStackHandler.setStackInSlot(f1,itemStacks.get(f1));
            }
        }

        energyStorage.setEnergy(tag.getInt("energy"));
        ticks = tag.getInt("ticks");
        multiblockFormed = tag.getBoolean("multiblock_formed");
        progress = tag.getInt("progress");

        if(tag.getString("fluid").isBlank() || tag.getString("fluid").isEmpty() || tag.getString("fluid").equals("minecraft:empty")){
            return;
        }

        String fluidName = tag.get("fluid").toString();

        if(BuiltInRegistries.FLUID.containsKey(ResourceLocation.parse(tag.getString("fluid")))){
            this.getFluidTank().setFluid(new FluidStack(
                    BuiltInRegistries.FLUID.get(ResourceLocation.parse(tag.getString("fluid"))),
                    tag.getInt("fluid_amount")));
        }
        else{
            Cmatd.getLogger().debug("{} Food Reactor: Fluid {} doesn't exist in registry, so it was skipped for this tank.",getBlockPos().toShortString(),fluidName);
        }
    }
}
