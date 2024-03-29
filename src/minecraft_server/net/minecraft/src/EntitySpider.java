package net.minecraft.src;

public class EntitySpider extends EntityMob {
	public EntitySpider(World world1) {
		super(world1);
		this.texture = "/mob/spider.png";
		this.setSize(1.4F, 0.9F);
		this.moveSpeed = 0.8F;
	}

	public double getMountedYOffset() {
		return (double)this.height * 0.75D - 0.5D;
	}

	protected Entity findPlayerToAttack() {
		float f1 = this.getBrightness(1.0F);
		if(f1 < 0.5F) {
			double d2 = 16.0D;
			return this.worldObj.getClosestPlayerToEntity(this, d2);
		} else {
			return null;
		}
	}

	protected String getLivingSound() {
		return "mob.spider";
	}

	protected String getHurtSound() {
		return "mob.spider";
	}

	protected String getDeathSound() {
		return "mob.spiderdeath";
	}

	protected void attackEntity(Entity entity, float damage) {
		float f3 = this.getBrightness(1.0F);
		if(f3 > 0.5F && this.rand.nextInt(100) == 0) {
			this.entityToAttack = null;
		} else {
			if(damage > 2.0F && damage < 6.0F && this.rand.nextInt(10) == 0) {
				if(this.onGround) {
					double d4 = entity.posX - this.posX;
					double d6 = entity.posZ - this.posZ;
					float f8 = MathHelper.sqrt_double(d4 * d4 + d6 * d6);
					this.motionX = d4 / (double)f8 * 0.5D * (double)0.8F + this.motionX * (double)0.2F;
					this.motionZ = d6 / (double)f8 * 0.5D * (double)0.8F + this.motionZ * (double)0.2F;
					this.motionY = (double)0.4F;
				}
			} else {
				super.attackEntity(entity, damage);
			}

		}
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
	}

	protected int getDropItemId() {
		return Item.silk.shiftedIndex;
	}
}
