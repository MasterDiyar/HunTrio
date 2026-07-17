package net.diyarnagibaster.huntrio.research;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashSet;
import java.util.Set;

public class ResearchProgress implements INBTSerializable<CompoundTag> {
    private final Set<String> unlockedResearches = new HashSet<>();

    public boolean unlock(String researchId) {
        return unlockedResearches.add(researchId);
    }

    public boolean isUnlocked(String researchId) {
        return unlockedResearches.contains(researchId);
    }

    public Set<String> getUnlocked() {
        return unlockedResearches;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (String id : unlockedResearches) {
            list.add(StringTag.valueOf(id));
        }
        tag.put("unlocked", list);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        unlockedResearches.clear();
        if (nbt.contains("unlocked", Tag.TAG_LIST)) {
            ListTag list = nbt.getList("unlocked", Tag.TAG_STRING);
            for (int i = 0; i < list.size(); i++) {
                unlockedResearches.add(list.getString(i));
            }
        }
    }
}
