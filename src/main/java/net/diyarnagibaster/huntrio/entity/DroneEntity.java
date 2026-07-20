package net.diyarnagibaster.huntrio.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DroneEntity extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> IS_ACTIVE = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MAY_BLOW = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.BOOLEAN);

    public double droneMotionX, droneMotionY, droneMotionZ, bladeRotation;
    public float roll = 0f; // Угол наклона по оси Z (Крен)

    public DroneEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_ACTIVE, false);
        builder.define(MAY_BLOW, false);
    }

    public void setMayBlow(boolean mayBlow) { this.entityData.set(MAY_BLOW, mayBlow); }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.entityData.get(MAY_BLOW)) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.0f, Level.ExplosionInteraction.MOB);
            this.discard();
            return true;
        }
        return super.hurt(source, amount);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2000.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    public boolean isActive() {
        return this.entityData.get(IS_ACTIVE);
    }

    public void setActive(boolean active) {
        this.entityData.set(IS_ACTIVE, active);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            if (isActive() && !this.onGround()) {
                bladeRotation += 20f;
                if (bladeRotation >= 360f)
                    bladeRotation -= 360f;
            }
        }
    }

    public void updateMovement(boolean w, boolean s, boolean a, boolean d, boolean space, boolean shift) {
        if (!this.isActive()) return;

        float tiltSpeed = 4.0f;
        if (w) this.setXRot(this.getXRot() - tiltSpeed);
        if (s) this.setXRot(this.getXRot() + tiltSpeed);
        if (a) roll -= tiltSpeed;
        if (d) roll += tiltSpeed;

        float adjustedPitch = this.getXRot() - 90.0f;

        float pitchRad = (float) Math.toRadians(adjustedPitch);
        float yawRad = (float) Math.toRadians(-this.getYRot());

        double thrustDirX = Math.sin(yawRad) * Math.cos(pitchRad);
        double thrustDirY = -Math.sin(pitchRad);
        double thrustDirZ = Math.cos(yawRad) * Math.cos(pitchRad);

        double thrust = 0.12D;

        if (space) {
            droneMotionX += thrustDirX * thrust;
            droneMotionY += thrustDirY * thrust;
            droneMotionZ += thrustDirZ * thrust;
        }

        if (shift) {
            droneMotionX *= 0.8D;
            droneMotionY *= 0.8D;
            droneMotionZ *= 0.8D;
        }

        droneMotionX *= 0.99D;
        droneMotionY *= 0.98D;
        droneMotionZ *= 0.99D;

        this.setDeltaMovement(droneMotionX, droneMotionY, droneMotionZ);
        this.hasImpulse = true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("mayBlow", this.entityData.get(MAY_BLOW));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(MAY_BLOW, tag.getBoolean("mayBlow"));
    }
}