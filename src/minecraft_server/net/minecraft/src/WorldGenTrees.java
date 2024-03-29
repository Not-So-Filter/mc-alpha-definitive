package net.minecraft.src;

import java.util.Random;

public class WorldGenTrees extends WorldGenerator {
	public boolean generate(World world, Random rand, int x, int y, int z) {
		int i6 = rand.nextInt(3) + 4;
		boolean z7 = true;
		if(y >= 1 && y + i6 + 1 <= 128) {
			int i8;
			int i10;
			int i11;
			int i12;
			for(i8 = y; i8 <= y + 1 + i6; ++i8) {
				byte b9 = 1;
				if(i8 == y) {
					b9 = 0;
				}

				if(i8 >= y + 1 + i6 - 2) {
					b9 = 2;
				}

				for(i10 = x - b9; i10 <= x + b9 && z7; ++i10) {
					for(i11 = z - b9; i11 <= z + b9 && z7; ++i11) {
						if(i8 >= 0 && i8 < 128) {
							i12 = world.getBlockId(i10, i8, i11);
							if(i12 != 0 && i12 != Block.leaves.blockID) {
								z7 = false;
							}
						} else {
							z7 = false;
						}
					}
				}
			}

			if(!z7) {
				return false;
			} else {
				i8 = world.getBlockId(x, y - 1, z);
				if((i8 == Block.grass.blockID || i8 == Block.dirt.blockID) && y < 128 - i6 - 1) {
					world.setBlock(x, y - 1, z, Block.dirt.blockID);

					int i16;
					for(i16 = y - 3 + i6; i16 <= y + i6; ++i16) {
						i10 = i16 - (y + i6);
						i11 = 1 - i10 / 2;

						for(i12 = x - i11; i12 <= x + i11; ++i12) {
							int i13 = i12 - x;

							for(int i14 = z - i11; i14 <= z + i11; ++i14) {
								int i15 = i14 - z;
								if((Math.abs(i13) != i11 || Math.abs(i15) != i11 || rand.nextInt(2) != 0 && i10 != 0) && !Block.opaqueCubeLookup[world.getBlockId(i12, i16, i14)]) {
									world.setBlock(i12, i16, i14, Block.leaves.blockID);
								}
							}
						}
					}

					for(i16 = 0; i16 < i6; ++i16) {
						i10 = world.getBlockId(x, y + i16, z);
						if(i10 == 0 || i10 == Block.leaves.blockID) {
							world.setBlock(x, y + i16, z, Block.wood.blockID);
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}
