package mod.surviving_the_aftermath.capability;

import mod.surviving_the_aftermath.init.ModCapability;
import mod.surviving_the_aftermath.raid.IRaid;
import mod.surviving_the_aftermath.raid.RaidFactory;
import mod.surviving_the_aftermath.raid.RaidManager;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RaidData implements INBTSerializable<CompoundTag> {
	public static final RaidManager instance = RaidManager.getInstance();
	private final ServerLevel level;
	public RaidData(ServerLevel level) {
		this.level = level;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag compoundTag = new CompoundTag();
		for (Map.Entry<UUID, IRaid> entry : instance.getRaids().entrySet()) {
			UUID uuid = entry.getKey();
			IRaid raid = entry.getValue();
			compoundTag.put(uuid.toString(), raid.serializeNBT());
		}
		System.out.println("RaidData serializeNBT");
		System.out.println("compoundTag :" + compoundTag);

		return compoundTag;
	}

	@Override
	public void deserializeNBT(CompoundTag compoundTag) {
		System.out.println("RaidData deserializeNBT");
		System.out.println("compoundTag :" + compoundTag);
		compoundTag.getAllKeys().forEach(key -> {
			instance.create(level, compoundTag.getCompound(key));
		});
	}

	public static LazyOptional<RaidData> get(Level level) {
		return level.getCapability(ModCapability.RAID_DATA);
	}

	public void tick(ServerLevel level) {
		instance.tick(level);
	}

	public static class Provider implements ICapabilitySerializable<CompoundTag> {

		private final LazyOptional<RaidData> instance;

		public Provider(ServerLevel level) {
			instance = LazyOptional.of(() -> new RaidData(level));
		}

		@Override
		@NotNull
		public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
			return ModCapability.RAID_DATA.orEmpty(cap, instance);
		}

		@Override
		public CompoundTag serializeNBT() {
			return instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")).serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")).deserializeNBT(nbt);
		}

	}
}