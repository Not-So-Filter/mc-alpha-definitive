package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerSP extends PlayerController {
	private int curBlockX = -1;
	private int curBlockY = -1;
	private int curBlockZ = -1;
	private float curBlockDamage = 0.0F;
	private float prevBlockDamage = 0.0F;
	private float blockDestroySoundCounter = 0.0F;
	private int blockHitWait = 0;
	private SpawnerAnimals monsterSpawner = new SpawnerClient(this, 200, IMobs.class, new Class[]{EntityZombie.class, EntitySkeleton.class, EntityCreeper.class, EntitySpider.class, EntitySlime.class});
	private SpawnerAnimals animalSpawner = new SpawnerAnimals(15, EntityAnimal.class, new Class[]{EntitySheep.class, EntityPig.class, EntityCow.class, EntityChicken.class});

	public PlayerControllerSP(Minecraft minecraft1) {
		super(minecraft1);
	}

	public void flipPlayer(EntityPlayer entityPlayer) {
		entityPlayer.rotationYaw = -180.0F;
	}

	public boolean sendBlockRemoved(int x, int y, int z, int side) {
		int i5 = this.mc.theWorld.getBlockId(x, y, z);
		int i6 = this.mc.theWorld.getBlockMetadata(x, y, z);
		boolean z7 = super.sendBlockRemoved(x, y, z, side);
		ItemStack itemStack8 = this.mc.thePlayer.getCurrentEquippedItem();
		boolean z9 = this.mc.thePlayer.canHarvestBlock(Block.blocksList[i5]);
		if(itemStack8 != null) {
			itemStack8.onDestroyBlock(i5, x, y, z);
			if(itemStack8.stackSize == 0) {
				itemStack8.onItemDestroyedByUse(this.mc.thePlayer);
				this.mc.thePlayer.destroyCurrentEquippedItem();
			}
		}

		if(z7 && z9) {
			Block.blocksList[i5].harvestBlock(this.mc.theWorld, x, y, z, i6);
		}

		return z7;
	}

	public void clickBlock(int x, int y, int z, int side) {
		int i5 = this.mc.theWorld.getBlockId(x, y, z);
		if(i5 > 0 && this.curBlockDamage == 0.0F) {
			Block.blocksList[i5].onBlockClicked(this.mc.theWorld, x, y, z, this.mc.thePlayer);
		}

		if(i5 > 0 && Block.blocksList[i5].blockStrength(this.mc.thePlayer) >= 1.0F) {
			this.sendBlockRemoved(x, y, z, side);
		}

	}

	public void resetBlockRemoving() {
		this.curBlockDamage = 0.0F;
		this.blockHitWait = 0;
	}

	public void sendBlockRemoving(int x, int y, int z, int side) {
		if(this.blockHitWait > 0) {
			--this.blockHitWait;
		} else {
			if(x == this.curBlockX && y == this.curBlockY && z == this.curBlockZ) {
				int i5 = this.mc.theWorld.getBlockId(x, y, z);
				if(i5 == 0) {
					return;
				}

				Block block6 = Block.blocksList[i5];
				this.curBlockDamage += block6.blockStrength(this.mc.thePlayer);
				if(this.blockDestroySoundCounter % 4.0F == 0.0F && block6 != null) {
					this.mc.sndManager.playSound(block6.stepSound.getStepSound(), (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, (block6.stepSound.getVolume() + 1.0F) / 8.0F, block6.stepSound.getPitch() * 0.5F);
				}

				++this.blockDestroySoundCounter;
				if(this.curBlockDamage >= 1.0F) {
					this.sendBlockRemoved(x, y, z, side);
					this.curBlockDamage = 0.0F;
					this.prevBlockDamage = 0.0F;
					this.blockDestroySoundCounter = 0.0F;
					this.blockHitWait = 5;
				}
			} else {
				this.curBlockDamage = 0.0F;
				this.prevBlockDamage = 0.0F;
				this.blockDestroySoundCounter = 0.0F;
				this.curBlockX = x;
				this.curBlockY = y;
				this.curBlockZ = z;
			}

		}
	}

	public void setPartialTime(float renderPartialTick) {
		if(this.curBlockDamage <= 0.0F) {
			this.mc.ingameGUI.damageGuiPartialTime = 0.0F;
			this.mc.renderGlobal.damagePartialTime = 0.0F;
		} else {
			float f2 = this.prevBlockDamage + (this.curBlockDamage - this.prevBlockDamage) * renderPartialTick;
			this.mc.ingameGUI.damageGuiPartialTime = f2;
			this.mc.renderGlobal.damagePartialTime = f2;
		}

	}

	public float getBlockReachDistance() {
		return 4.0F;
	}

	public void onWorldChange(World world) {
		super.onWorldChange(world);
	}

	public void onUpdate() {
		this.prevBlockDamage = this.curBlockDamage;
		this.monsterSpawner.onUpdate(this.mc.theWorld);
		this.animalSpawner.onUpdate(this.mc.theWorld);
		this.mc.sndManager.playRandomMusicIfReady();
	}
}
