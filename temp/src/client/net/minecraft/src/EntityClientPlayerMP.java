package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class EntityClientPlayerMP extends EntityPlayerSP {
	private NetClientHandler sendQueue;
	private int motionUpdateCounter = 0;
	private double oldPosX;
	private double oldBasePos;
	private double oldPosY;
	private double oldPosZ;
	private float oldRotationYaw;
	private float oldRotationPitch;
	private InventoryPlayer serverSideInventory = new InventoryPlayer((EntityPlayer)null);

	public EntityClientPlayerMP(Minecraft minecraft, World worldObj, Session session, NetClientHandler sendQueue) {
		super(minecraft, worldObj, session);
		this.sendQueue = sendQueue;
	}

	public void onUpdate() {
		super.onUpdate();
		this.sendMotionUpdates();
	}

	public void onPlayerUpdate() {
		this.sendMotionUpdates();
	}

	public void sendMotionUpdates() {
		if(this.motionUpdateCounter++ == 20) {
			if(!this.inventory.getInventoryEqual(this.serverSideInventory)) {
				this.sendQueue.addToSendQueue(new Packet5PlayerInventory(-1, this.inventory.mainInventory));
				this.sendQueue.addToSendQueue(new Packet5PlayerInventory(-2, this.inventory.craftingInventory));
				this.sendQueue.addToSendQueue(new Packet5PlayerInventory(-3, this.inventory.armorInventory));
				this.serverSideInventory = this.inventory.copyInventory();
			}

			this.motionUpdateCounter = 0;
		}

		double d1 = this.posX - this.oldPosX;
		double d3 = this.boundingBox.minY - this.oldBasePos;
		double d5 = this.posY - this.oldPosY;
		double d7 = this.posZ - this.oldPosZ;
		double d9 = (double)(this.rotationYaw - this.oldRotationYaw);
		double d11 = (double)(this.rotationPitch - this.oldRotationPitch);
		boolean z13 = d3 != 0.0D || d5 != 0.0D || d1 != 0.0D || d7 != 0.0D;
		boolean z14 = d9 != 0.0D || d11 != 0.0D;
		if(z13 && z14) {
			this.sendQueue.addToSendQueue(new Packet13PlayerLookMove(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
		} else if(z13) {
			this.sendQueue.addToSendQueue(new Packet11PlayerPosition(this.posX, this.boundingBox.minY, this.posY, this.posZ, this.onGround));
		} else if(z14) {
			this.sendQueue.addToSendQueue(new Packet12PlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
		} else {
			this.sendQueue.addToSendQueue(new Packet10Flying(this.onGround));
		}

		if(z13) {
			this.oldPosX = this.posX;
			this.oldBasePos = this.boundingBox.minY;
			this.oldPosY = this.posY;
			this.oldPosZ = this.posZ;
		}

		if(z14) {
			this.oldRotationYaw = this.rotationYaw;
			this.oldRotationPitch = this.rotationPitch;
		}

	}

	protected void joinEntityItemWithWorld(EntityItem entityItem) {
		System.out.println("Dropping?");
		Packet21PickupSpawn packet21PickupSpawn2 = new Packet21PickupSpawn(entityItem);
		this.sendQueue.addToSendQueue(packet21PickupSpawn2);
		entityItem.posX = (double)packet21PickupSpawn2.xPosition / 32.0D;
		entityItem.posY = (double)packet21PickupSpawn2.yPosition / 32.0D;
		entityItem.posZ = (double)packet21PickupSpawn2.zPosition / 32.0D;
		entityItem.motionX = (double)packet21PickupSpawn2.rotation / 128.0D;
		entityItem.motionY = (double)packet21PickupSpawn2.pitch / 128.0D;
		entityItem.motionZ = (double)packet21PickupSpawn2.roll / 128.0D;
	}

	public void sendChatMessage(String chatMessage) {
		this.sendQueue.addToSendQueue(new Packet3Chat(chatMessage));
	}

	public void swingItem() {
		super.swingItem();
		this.sendQueue.addToSendQueue(new Packet18ArmAnimation(this, 1));
	}
}
