package net.diyarnagibaster.huntrio.entity;

import net.diyarnagibaster.huntrio.HunTrio;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModEntities {
    // Создаем реестр для сущностей
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, HunTrio.MODID);

    // Регистрируем нашего Робота
    public static final Supplier<EntityType<DroneEntity>> DRONE_MK1 = ENTITY_TYPES.register("robot",
            () -> EntityType.Builder.of(DroneEntity::new, MobCategory.CREATURE)
                    .sized(0.4f, 0.4f)
                    .clientTrackingRange(20)
                    .updateInterval(2)
                    .build("robot"));
}