package net.minecraft.src;

public class ItemTool extends Item {
	private Block[] blocksEffectiveAgainst;
	private float efficiencyOnProperMaterial = 4.0F;
	private int damageVsEntity;
	protected int toolMaterial;

	public ItemTool(int id, int attackDmg, int strength, Block[] blocks) {
		super(id);
		this.toolMaterial = strength;
		this.blocksEffectiveAgainst = blocks;
		this.maxStackSize = 1;
		this.maxDamage = 32 << strength;
		if(strength == 3) {
			this.maxDamage *= 4;
		}

		this.efficiencyOnProperMaterial = (float)((strength + 1) * 2);
		this.damageVsEntity = attackDmg + strength;
	}

	public float getStrVsBlock(ItemStack itemStack, Block block) {
		for(int i3 = 0; i3 < this.blocksEffectiveAgainst.length; ++i3) {
			if(this.blocksEffectiveAgainst[i3] == block) {
				return this.efficiencyOnProperMaterial;
			}
		}

		return 1.0F;
	}

	public void hitEntity(ItemStack itemStack, EntityLiving entityLiving) {
		itemStack.damageItem(2);
	}

	public void onBlockDestroyed(ItemStack itemStack, int id, int x, int y, int z) {
		itemStack.damageItem(1);
	}

	public int getDamageVsEntity(Entity entity) {
		return this.damageVsEntity;
	}

	public boolean isFull3D() {
		return true;
	}
}
