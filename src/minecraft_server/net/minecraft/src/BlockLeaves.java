package net.minecraft.src;

import java.util.Random;

public class BlockLeaves extends BlockLeavesBase {
	private int leafTexIndex;
	private int decayCounter = 0;

	protected BlockLeaves(int id, int blockIndex) {
		super(id, blockIndex, Material.leaves, false);
		this.leafTexIndex = blockIndex;
		this.setTickOnLoad(true);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		this.decayCounter = 0;
		this.updateCurrentLeaves(world, x, y, z);
		super.onNeighborBlockChange(world, x, y, z, flag);
	}

	public void updateConnectedLeaves(World world, int x, int y, int z, int i5) {
		if(world.getBlockId(x, y, z) == this.blockID) {
			int i6 = world.getBlockMetadata(x, y, z);
			if(i6 != 0 && i6 == i5 - 1) {
				this.updateCurrentLeaves(world, x, y, z);
			}
		}
	}

	public void updateCurrentLeaves(World world, int x, int y, int z) {
		if(this.decayCounter++ < 100) {
			int i5 = world.getBlockMaterial(x, y - 1, z).isSolid() ? 16 : 0;
			int i6 = world.getBlockMetadata(x, y, z);
			if(i6 == 0) {
				i6 = 1;
				world.setBlockMetadataWithNotify(x, y, z, 1);
			}

			i5 = this.getConnectionStrength(world, x, y - 1, z, i5);
			i5 = this.getConnectionStrength(world, x, y, z - 1, i5);
			i5 = this.getConnectionStrength(world, x, y, z + 1, i5);
			i5 = this.getConnectionStrength(world, x - 1, y, z, i5);
			i5 = this.getConnectionStrength(world, x + 1, y, z, i5);
			int i7 = i5 - 1;
			if(i7 < 10) {
				i7 = 1;
			}

			if(i7 != i6) {
				world.setBlockMetadataWithNotify(x, y, z, i7);
				this.updateConnectedLeaves(world, x, y - 1, z, i6);
				this.updateConnectedLeaves(world, x, y + 1, z, i6);
				this.updateConnectedLeaves(world, x, y, z - 1, i6);
				this.updateConnectedLeaves(world, x, y, z + 1, i6);
				this.updateConnectedLeaves(world, x - 1, y, z, i6);
				this.updateConnectedLeaves(world, x + 1, y, z, i6);
			}

		}
	}

	private int getConnectionStrength(World world, int x, int y, int z, int i5) {
		int i6 = world.getBlockId(x, y, z);
		if(i6 == Block.wood.blockID) {
			return 16;
		} else {
			if(i6 == this.blockID) {
				int i7 = world.getBlockMetadata(x, y, z);
				if(i7 != 0 && i7 > i5) {
					return i7;
				}
			}

			return i5;
		}
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		int i6 = world.getBlockMetadata(x, y, z);
		if(i6 == 0) {
			this.decayCounter = 0;
			this.updateCurrentLeaves(world, x, y, z);
		} else if(i6 == 1) {
			this.removeLeaves(world, x, y, z);
		} else if(random.nextInt(10) == 0) {
			this.updateCurrentLeaves(world, x, y, z);
		}

	}

	private void removeLeaves(World world, int x, int y, int z) {
		this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z));
		world.setBlockWithNotify(x, y, z, 0);
	}

	public int quantityDropped(Random random) {
		return random.nextInt(20) == 0 ? 1 : 0;
	}

	public int idDropped(int count, Random random) {
		return Block.sapling.blockID;
	}

	public boolean isOpaqueCube() {
		return !this.graphicsLevel;
	}

	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		super.onEntityWalking(world, x, y, z, entity);
	}
}
