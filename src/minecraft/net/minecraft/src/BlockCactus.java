package net.minecraft.src;

import java.util.Random;

public class BlockCactus extends Block {
	protected BlockCactus(int id, int tex) {
		super(id, tex, Material.cactus);
		this.setTickOnLoad(true);
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		if(worldObj.getBlockId(x, y + 1, z) == 0) {
			int i6;
			for(i6 = 1; worldObj.getBlockId(x, y - i6, z) == this.blockID; ++i6) {
			}

			if(i6 < 3) {
				int i7 = worldObj.getBlockMetadata(x, y, z);
				if(i7 == 15) {
					worldObj.setBlockWithNotify(x, y + 1, z, this.blockID);
					worldObj.setBlockMetadataWithNotify(x, y, z, 0);
				} else {
					worldObj.setBlockMetadataWithNotify(x, y, z, i7 + 1);
				}
			}
		}

	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		float f5 = 0.0625F;
		return AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + f5), (double)y, (double)((float)z + f5), (double)((float)(x + 1) - f5), (double)((float)(y + 1) - f5), (double)((float)(z + 1) - f5));
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldObj, int x, int y, int z) {
		float f5 = 0.0625F;
		return AxisAlignedBB.getBoundingBoxFromPool((double)((float)x + f5), (double)y, (double)((float)z + f5), (double)((float)(x + 1) - f5), (double)(y + 1), (double)((float)(z + 1) - f5));
	}

	public int getBlockTextureFromSide(int side) {
		return side == 1 ? this.blockIndexInTexture - 1 : (side == 0 ? this.blockIndexInTexture + 1 : this.blockIndexInTexture);
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return 13;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return !super.canPlaceBlockAt(world, x, y, z) ? false : this.canBlockStay(world, x, y, z);
	}

	public void onNeighborBlockChange(World worldObj, int x, int y, int z, int id) {
		if(!this.canBlockStay(worldObj, x, y, z)) {
			this.dropBlockAsItem(worldObj, x, y, z, worldObj.getBlockMetadata(x, y, z));
			worldObj.setBlockWithNotify(x, y, z, 0);
		}

	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		if(world.getBlockMaterial(x - 1, y, z).isSolid()) {
			return false;
		} else if(world.getBlockMaterial(x + 1, y, z).isSolid()) {
			return false;
		} else if(world.getBlockMaterial(x, y, z - 1).isSolid()) {
			return false;
		} else if(world.getBlockMaterial(x, y, z + 1).isSolid()) {
			return false;
		} else {
			int i5 = world.getBlockId(x, y - 1, z);
			return i5 == Block.cactus.blockID || i5 == Block.sand.blockID;
		}
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.attackEntityFrom((Entity)null, 1);
	}
}
