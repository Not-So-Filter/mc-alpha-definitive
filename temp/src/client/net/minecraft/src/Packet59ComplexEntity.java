package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet59ComplexEntity extends Packet {
	public int xCoord;
	public int yCoord;
	public int zCoord;
	public byte[] compressedNBT;
	public NBTTagCompound tileEntityNBT;

	public Packet59ComplexEntity() {
		this.isChunkDataPacket = true;
	}

	public Packet59ComplexEntity(int x, int y, int z, TileEntity tileEntity) {
		this.isChunkDataPacket = true;
		this.xCoord = x;
		this.yCoord = y;
		this.zCoord = z;
		this.tileEntityNBT = new NBTTagCompound();
		tileEntity.writeToNBT(this.tileEntityNBT);

		try {
			this.compressedNBT = CompressedStreamTools.compress(this.tileEntityNBT);
		} catch (IOException iOException6) {
			iOException6.printStackTrace();
		}

	}

	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		this.xCoord = dataInputStream.readInt();
		this.yCoord = dataInputStream.readShort();
		this.zCoord = dataInputStream.readInt();
		int i2 = dataInputStream.readShort() & 65535;
		this.compressedNBT = new byte[i2];
		dataInputStream.readFully(this.compressedNBT);
		this.tileEntityNBT = CompressedStreamTools.decompress(this.compressedNBT);
	}

	public void writePacket(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(this.xCoord);
		dataOutputStream.writeShort(this.yCoord);
		dataOutputStream.writeInt(this.zCoord);
		dataOutputStream.writeShort((short)this.compressedNBT.length);
		dataOutputStream.write(this.compressedNBT);
	}

	public void processPacket(NetHandler netHandler) {
		netHandler.handleComplexEntity(this);
	}

	public int getPacketSize() {
		return this.compressedNBT.length + 2 + 10;
	}
}
