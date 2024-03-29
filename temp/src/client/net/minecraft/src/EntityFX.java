package net.minecraft.src;

public class EntityFX extends Entity {
	protected int particleTextureIndex;
	protected float particleTextureJitterX;
	protected float particleTextureJitterY;
	protected int particleAge = 0;
	protected int particleMaxAge = 0;
	protected float particleScale;
	protected float particleGravity;
	protected float particleRed;
	protected float particleGreen;
	protected float particleBlue;
	public static double interpPosX;
	public static double interpPosY;
	public static double interpPosZ;

	public EntityFX(World worldObj, double x, double y, double z, double motionX, double motionY, double motionZ) {
		super(worldObj);
		this.setSize(0.2F, 0.2F);
		this.yOffset = this.height / 2.0F;
		this.setPosition(x, y, z);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.motionX = motionX + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		this.motionY = motionY + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		this.motionZ = motionZ + (double)((float)(Math.random() * 2.0D - 1.0D) * 0.4F);
		float f14 = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
		float f15 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
		this.motionX = this.motionX / (double)f15 * (double)f14 * (double)0.4F;
		this.motionY = this.motionY / (double)f15 * (double)f14 * (double)0.4F + (double)0.1F;
		this.motionZ = this.motionZ / (double)f15 * (double)f14 * (double)0.4F;
		this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
		this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
		this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
		this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
		this.particleAge = 0;
		this.canTriggerWalking = false;
	}

	public EntityFX multiplyVelocity(float velocityMultiplier) {
		this.motionX *= (double)velocityMultiplier;
		this.motionY = (this.motionY - (double)0.1F) * (double)velocityMultiplier + (double)0.1F;
		this.motionZ *= (double)velocityMultiplier;
		return this;
	}

	public EntityFX multipleParticleScaleBy(float particleScaleMultiplier) {
		this.setSize(0.2F * particleScaleMultiplier, 0.2F * particleScaleMultiplier);
		this.particleScale *= particleScaleMultiplier;
		return this;
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.particleAge++ >= this.particleMaxAge) {
			this.setEntityDead();
		}

		this.motionY -= 0.04D * (double)this.particleGravity;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		this.motionX *= (double)0.98F;
		this.motionY *= (double)0.98F;
		this.motionZ *= (double)0.98F;
		if(this.onGround) {
			this.motionX *= (double)0.7F;
			this.motionZ *= (double)0.7F;
		}

	}

	public void renderParticle(Tessellator tessellator, float renderPartialTick, float xOffset, float yOffset, float zOffset, float xOffset2, float zOffset2) {
		float f8 = (float)(this.particleTextureIndex % 16) / 16.0F;
		float f9 = f8 + 0.0624375F;
		float f10 = (float)(this.particleTextureIndex / 16) / 16.0F;
		float f11 = f10 + 0.0624375F;
		float f12 = 0.1F * this.particleScale;
		float f13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)renderPartialTick - interpPosX);
		float f14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)renderPartialTick - interpPosY);
		float f15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)renderPartialTick - interpPosZ);
		float f16 = this.getBrightness(renderPartialTick);
		tessellator.setColorOpaque_F(this.particleRed * f16, this.particleGreen * f16, this.particleBlue * f16);
		tessellator.addVertexWithUV((double)(f13 - xOffset * f12 - xOffset2 * f12), (double)(f14 - yOffset * f12), (double)(f15 - zOffset * f12 - zOffset2 * f12), (double)f8, (double)f11);
		tessellator.addVertexWithUV((double)(f13 - xOffset * f12 + xOffset2 * f12), (double)(f14 + yOffset * f12), (double)(f15 - zOffset * f12 + zOffset2 * f12), (double)f8, (double)f10);
		tessellator.addVertexWithUV((double)(f13 + xOffset * f12 + xOffset2 * f12), (double)(f14 + yOffset * f12), (double)(f15 + zOffset * f12 + zOffset2 * f12), (double)f9, (double)f10);
		tessellator.addVertexWithUV((double)(f13 + xOffset * f12 - xOffset2 * f12), (double)(f14 - yOffset * f12), (double)(f15 + zOffset * f12 - zOffset2 * f12), (double)f9, (double)f11);
	}

	public int getFXLayer() {
		return 0;
	}

	public void writeEntityToNBT(NBTTagCompound compoundTag) {
	}

	public void readEntityFromNBT(NBTTagCompound compoundTag) {
	}
}
