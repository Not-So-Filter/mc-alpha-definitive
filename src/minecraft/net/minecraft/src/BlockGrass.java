package net.minecraft.src;

import java.util.Random;

public class BlockGrass extends Block {
	protected BlockGrass(int blockID) {
		super(blockID, Material.grass);
		this.blockIndexInTexture = 3;
		this.setTickOnLoad(true);
	}

	public int getBlockTexture(IBlockAccess blockAccess, int x, int y, int z, int side) {
		if(side == 1) {
			return 0;
		} else if(side == 0) {
			return 2;
		} else {
			Material material6 = blockAccess.getBlockMaterial(x, y + 1, z);
			return material6 != Material.snow && material6 != Material.craftedSnow ? 3 : 68;
		}
	}

	public void updateTick(World worldObj, int x, int y, int z, Random rand) {
		if(worldObj.getBlockLightValue(x, y + 1, z) < 4 && worldObj.getBlockMaterial(x, y + 1, z).getCanBlockGrass()) {
			if(rand.nextInt(4) != 0) {
				return;
			}

			worldObj.setBlockWithNotify(x, y, z, Block.dirt.blockID);
		} else if(worldObj.getBlockLightValue(x, y + 1, z) >= 9) {
			int i6 = x + rand.nextInt(3) - 1;
			int i7 = y + rand.nextInt(5) - 3;
			int i8 = z + rand.nextInt(3) - 1;
			if(worldObj.getBlockId(i6, i7, i8) == Block.dirt.blockID && worldObj.getBlockLightValue(i6, i7 + 1, i8) >= 4 && !worldObj.getBlockMaterial(i6, i7 + 1, i8).getCanBlockGrass()) {
				worldObj.setBlockWithNotify(i6, i7, i8, Block.grass.blockID);
			}
		}

	}

	public int idDropped(int metadata, Random rand) {
		return Block.dirt.idDropped(0, rand);
	}
}
