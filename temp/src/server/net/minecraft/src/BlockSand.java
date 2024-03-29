package net.minecraft.src;

import java.util.Random;

public class BlockSand extends Block {
	public static boolean fallInstantly = false;

	public BlockSand(int id, int blockIndex) {
		super(id, blockIndex, Material.sand);
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		world.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int flag) {
		world.scheduleBlockUpdate(x, y, z, this.blockID);
	}

	public void updateTick(World world, int x, int y, int z, Random random) {
		this.tryToFall(world, x, y, z);
	}

	private void tryToFall(World world, int x, int y, int z) {
		if(canFallBelow(world, x, y - 1, z) && y >= 0) {
			EntityFallingSand entityFallingSand8 = new EntityFallingSand(world, (float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, this.blockID);
			if(fallInstantly) {
				while(!entityFallingSand8.isDead) {
					entityFallingSand8.onUpdate();
				}
			} else {
				world.spawnEntityInWorld(entityFallingSand8);
			}
		}

	}

	public int tickRate() {
		return 3;
	}

	public static boolean canFallBelow(World world, int x, int y, int z) {
		int i4 = world.getBlockId(x, y, z);
		if(i4 == 0) {
			return true;
		} else if(i4 == Block.fire.blockID) {
			return true;
		} else {
			Material material5 = Block.blocksList[i4].material;
			return material5 == Material.water ? true : material5 == Material.lava;
		}
	}
}
