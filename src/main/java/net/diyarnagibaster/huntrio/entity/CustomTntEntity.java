package net.diyarnagibaster.huntrio.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import javax.annotation.Nullable;

public class CustomTntEntity extends PrimedTnt {

    @Nullable
    private LivingEntity owner;

    public CustomTntEntity(EntityType<? extends CustomTntEntity> type, Level level) {
        super(type, level);
    }

    public CustomTntEntity(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(ModEntities.CUSTOM_TNT.get(), level);
        this.setPos(x, y, z);
        double d0 = level.random.nextDouble() * (Math.PI * 2);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2D, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;

        this.owner = owner;
    }

    @Override
    @Nullable
    public LivingEntity getOwner() {
        return this.owner;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.owner = owner;
    }

    @Override
    protected void explode() {
        float explosionPower = 16.0F;

        this.level().explode(
                this,
                this.getX(),
                this.getY(0.0625D),
                this.getZ(),
                explosionPower,
                ExplosionInteraction.TNT
        );
    }
}