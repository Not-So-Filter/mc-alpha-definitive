package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ThreadSleepForeverServer extends Thread {
	final MinecraftServer mcServer;

	public ThreadSleepForeverServer(MinecraftServer minecraftServer) {
		this.mcServer = minecraftServer;
		this.setDaemon(true);
		this.start();
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(2147483647L);
			} catch (InterruptedException interruptedException2) {
			}
		}
	}
}
