package net.bcm.cmatd.blockentity;

import net.bcm.cmatd.BaseEnergyStorage;
import net.bcm.cmatd.CmatdSound;
import net.bcm.cmatd.gui.BaseEnergyMakerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class BaseEnergyMakerBE extends BlockEntity implements MenuProvider {
    private int ticks = 0;
    private final BaseEnergyStorage energyStorage =
            new BaseEnergyStorage(10000,1000,1000){
                @Override
                public boolean canReceive() {
                    return true;
                }
            };
    private final Lazy<IEnergyStorage> lazyEnergyStorage = Lazy.of(() ->
            energyStorage);

    public int energy,energyProduction;

    private NonNullList<ItemStack> itemList = NonNullList.withSize(1, ItemStack.EMPTY);

    private final ItemStackHandler items = new ItemStackHandler(){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final ContainerData machineData;

    public static final int GENERATE = 50;
    public static final int CAPACITY = 10000;

    public static int SLOT_COUNT = 1;
    public static int SLOT = 0;

    private int burnTime;

    private int machine_tier = 0;

    public int getBurnTime(){
        return burnTime;
    }

    public BaseEnergyMakerBE(BlockPos pos, BlockState blockState){
        super(CmatdBE.BASE_ENERGY_MAKER_BE.get(), pos, blockState);
        this.machineData = new ContainerData() {
            @Override
            public int get(int index) {
                return switch(index){
                    case 0 -> energyStorage.getEnergyStored() & 0xFFFF;
                    case 1 -> energyStorage.getEnergyStored() >> 16;
                    default -> throw new IllegalArgumentException("index not in bounds " + index);
                };
            }

            @Override
            public void set(int index, int value) {
                switch(index){
                    case 0 -> energyStorage.setEnergy((energyStorage.getEnergyStored() & 0xFFFF0000) | (value & 0xFFFF));
                    case 1 -> energyStorage.setEnergy((energyStorage.getEnergyStored() & 0xFFFF) | (value << 16));
                    default -> throw new IllegalArgumentException("index not in bounds " + index);
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public BaseEnergyStorage getEnergyObject(){
        return energyStorage;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Energy Maker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BaseEnergyMakerMenu(i,player,getBlockPos());
    }

    public void serverTick(){
        ticks++;
        if(!(getBlockEntity().getLevel() == null)){
            if(getBlockEntity().getLevel().getGameTime() % 2 == 0){
                boolean saturated = energyStorage.isSaturatedEnergy();
                boolean burnTimeNotZero = burnTime > 0;
                level.setBlockAndUpdate(getBlockPos(),getBlockState().setValue(BlockStateProperties.POWERED,burnTimeNotZero));
                setChanged();
            }
            if(getBlockEntity().getLevel().getGameTime() % 80L == 0L && getBurnTime() > 0){
                getBlockEntity().getLevel().playSound(null,this.getBlockPos(),
                        CmatdSound.HEATER_LOOP.value(), SoundSource.BLOCKS,0.25f,1.0f);
            }
        }
        if(ticks > 32767){
            ticks = 0;
        }
        generateEnergy();
        distributeEnergy();
    }

    private void setBurnTime(int bt) {
        if (bt == burnTime) {
            return;
        }
        burnTime = bt;
    }

    private void generateEnergy() {
        if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
            if (burnTime <= 0) {
                ItemStack fuel = items.getStackInSlot(SLOT);
                // no fuel
                if (fuel.isEmpty()) {
                    return;
                }
                setBurnTime(fuel.getBurnTime(RecipeType.SMELTING));
                // not fuel
                if (burnTime <= 0) {
                    return;
                }
                items.extractItem(SLOT, 1, false);
            }
            else{
                setBurnTime(burnTime - 1);
                int generation_rate = GENERATE * (machine_tier + 1); // generate power based on tier and base amt + 1 to prevent +0 energy gen
                energyStorage.receiveEnergy(generation_rate, false);
            }
            setChanged();
        }
    }

    public void upgradeTier(){
        this.machine_tier += 1;
        setChanged();
    }

    public void upgradeToTier(int tier){
        this.machine_tier = tier;
        setChanged();
    }

    private void distributeEnergy() {
        for (Direction direction : Direction.values()) {
            if (energyStorage.getEnergyStored() <= 0) {
                return;
            }
            // get blocks that are energy capable
            IEnergyStorage energy = level.getCapability(Capabilities.EnergyStorage.BLOCK,
                    getBlockPos().relative(direction), null);
            if (energy != null) {
                if (energy.canReceive()) {
                    int received = energy.receiveEnergy(Math.min(
                            this.energyStorage.getEnergyStored(),
                            energyStorage.getMaxExtract()), false);
                    this.energyStorage.extractEnergy(received, false);
                    setChanged();
                }
            }
        }
    }

    public NonNullList<ItemStack> getItemStackList(){
        return this.itemList;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.itemList.set(0,this.items.getStackInSlot(0));
        ContainerHelper.saveAllItems(tag,this.itemList,registries);
        tag.putInt("machine_tier",machine_tier);
        tag.putInt("energy",energyStorage.getEnergyStored());
        tag.putInt("burn_time",burnTime);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.itemList = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag,this.itemList,registries);
        if(tag.contains("machine_tier")){
            tag.getInt("machine_tier");
        }

        if(tag.contains("energy")){
            energyStorage.setEnergy(tag.getInt("energy"));
        }
        burnTime = tag.getInt("burn_time");
    }

    public BlockEntity getBlockEntity(){
        return this;
    }

    public IItemHandler getItemHandler(){
        return items;
    }

    public int getMaxEnergy(){
        return this.energyStorage.getCapacity();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public IEnergyStorage getEnergyStorage() {
        return lazyEnergyStorage.get();
    }
}
