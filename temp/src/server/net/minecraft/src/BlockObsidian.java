package net.minecraft.src;

import java.util.Random;

public class BlockObsidian extends BlockStone {
	public BlockObsidian(int i1, int i2) {
		super(i1, i2);
	}

	public int quantityDropped(Random random) {
		return 1;
	}

	public int idDropped(int count, Random random) {
		return Block.obsidian.blockID;
	}
}
