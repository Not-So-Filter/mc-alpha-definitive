package net.minecraft.src;

public class EntityPig extends EntityAnimal {
	public boolean saddled = false;

	public EntityPig(World world1) {
		super(world1);
		this.texture = "/mob/pig.png";
		this.setSize(0.9F, 0.9F);
		this.saddled = false;
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Saddle", this.saddled);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		this.saddled = nbttagcompound.getBoolean("Saddle");
	}

	protected String getLivingSound() {
		return "mob.pig";
	}

	protected String getHurtSound() {
		return "mob.pig";
	}

	protected String getDeathSound() {
		return "mob.pigdeath";
	}

	protected int getDropItemId() {
		return Item.porkRaw.shiftedIndex;
	}
}
