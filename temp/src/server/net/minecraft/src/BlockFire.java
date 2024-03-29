package net.minecraft.src;

import java.util.Random;

public class BlockFire extends Block {
	private int[] chanceToEncourageFire = new int[256];
	private int[] abilityToCatchFire = new int[256];

	protected BlockFire(int id, int blockIndex) {
		super(id, blockIndex, Material.fire);
		this.initializeBlock(Block.planks.blockID, 5, 20);
		this.initializeBlock(Block.wood.blockID, 5, 5);
		this.initializeBlock(Block.leaves.blockID, 30, 60);
		this.initializeBlock(Block.bookshelf.blockID, 30, 20);
		this.initializeBlock(Block.tnt.blockID, 15, 100);
		this.initializeBlock(Block.cloth.blockID, 30, 60);
		this.setTickOnLoad(true);
	}

	private void initializeBlock(int blockID, int chance, int ability) {
		this.chanceToEncourageFire[blockID] = chance;
		this.abilityToCatchFire[blockID] = ability;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 3;
	}

	public int quantityDropped(Random random) {
		return 0;
	}

	public int tickRate() {
		return 10;
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		int i6 = world.getBlockMetadata(x, y, z);
		if(i6 < 15) {
			world.setBlockMetadataWithNotify(x, y, z, i6 + 1);
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}

		if(!this.canNeighborBurn(world, x, y, z)) {
			if(!world.isBlockNormalCube(x, y - 1, z) || i6 > 3) {
				world.setBlockWithNotify(x, y, z, 0);
			}

		} else if(!this.canBlockCatchFire(world, x, y - 1, z) && i6 == 15 && random.nextInt(4) == 0) {
			world.setBlockWithNotify(x, y, z, 0);
		} else {
			if(i6 % 2 == 0 && i6 > 2) {
				this.tryToCatchBlockOnFire(world, x + 1, y, z, 300, random);
				this.tryToCatchBlockOnFire(world, x - 1, y, z, 300, random);
				this.tryToCatchBlockOnFire(world, x, y - 1, z, 200, random);
				this.tryToCatchBlockOnFire(world, x, y + 1, z, 250, random);
				this.tryToCatchBlockOnFire(world, x, y, z - 1, 300, random);
				this.tryToCatchBlockOnFire(world, x, y, z + 1, 300, random);

				for(int i7 = x - 1; i7 <= x + 1; ++i7) {
					for(int i8 = z - 1; i8 <= z + 1; ++i8) {
						for(int i9 = y - 1; i9 <= y + 4; ++i9) {
							if(i7 != x || i9 != y || i8 != z) {
								int i10 = 100;
								if(i9 > y + 1) {
									i10 += (i9 - (y + 1)) * 100;
								}

								int i11 = this.getChanceOfNeighborsEncouragingFire(world, i7, i9, i8);
								if(i11 > 0 && random.nextInt(i10) <= i11) {
									world.setBlockWithNotify(i7, i9, i8, this.blockID);
								}
							}
						}
					}
				}
			}

		}
	}

	private void tryToCatchBlockOnFire(World world, int x, int y, int z, int chance, Random random) {
		int i7 = this.abilityToCatchFire[world.getBlockId(x, y, z)];
		if(random.nextInt(chance) < i7) {
			boolean z8 = world.getBlockId(x, y, z) == Block.tnt.blockID;
			if(random.nextInt(2) == 0) {
				world.setBlockWithNotify(x, y, z, this.blockID);
			} else {
				world.setBlockWithNotify(x, y, z, 0);
			}

			if(z8) {
				Block.tnt.onBlockDestroyedByPlayer(world, x, y, z, 0);
			}
		}

	}

	private boolean canNeighborBurn(World world, int x, int y, int z) {
		return this.canBlockCatchFire(world, x + 1, y, z) ? true : (this.canBlockCatchFire(world, x - 1, y, z) ? true : (this.canBlockCatchFire(world, x, y - 1, z) ? true : (this.canBlockCatchFire(world, x, y + 1, z) ? true : (this.canBlockCatchFire(world, x, y, z - 1) ? true : this.canBlockCatchFire(world, x, y, z + 1)))));
	}

	private int getChanceOfNeighborsEncouragingFire(World world, int x, int y, int z) {
		byte b5 = 0;
		if(world.getBlockId(x, y, z) != 0) {
			return 0;
		} else {
			int i6 = this.getChanceToEncourageFire(world, x + 1, y, z, b5);
			i6 = this.getChanceToEncourageFire(world, x - 1, y, z, i6);
			i6 = this.getChanceToEncourageFire(world, x, y - 1, z, i6);
			i6 = this.getChanceToEncourageFire(world, x, y + 1, z, i6);
			i6 = this.getChanceToEncourageFire(world, x, y, z - 1, i6);
			i6 = this.getChanceToEncourageFire(world, x, y, z + 1, i6);
			return i6;
		}
	}

	public boolean isCollidable() {
		return false;
	}

	public boolean canBlockCatchFire(IBlockAccess blockAccess, int x, int y, int z) {
		return this.chanceToEncourageFire[blockAccess.getBlockId(x, y, z)] > 0;
	}

	public int getChanceToEncourageFire(World world, int x, int y, int z, int flag) {
		int i6 = this.chanceToEncourageFire[world.getBlockId(x, y, z)];
		return i6 > flag ? i6 : flag;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.isBlockNormalCube(x, y - 1, z) || this.canNeighborBurn(world, x, y, z);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		if(!world.isBlockNormalCube(x, y - 1, z) && !this.canNeighborBurn(world, x, y, z)) {
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		if(!world.isBlockNormalCube(x, y - 1, z) && !this.canNeighborBurn(world, x, y, z)) {
			world.setBlockWithNotify(x, y, z, 0);
		} else {
			world.scheduleBlockUpdate(x, y, z, this.blockID);
		}
	}
}
