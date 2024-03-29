package net.minecraft.src;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Frame;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;

public final class MinecraftImpl extends Minecraft {
	final Frame mcFrame;

	public MinecraftImpl(Component component, Canvas canvas, MinecraftApplet mcApplet, int width, int height, boolean fullscreen, Frame frame) {
		super(component, canvas, mcApplet, width, height, fullscreen);
		this.mcFrame = frame;
	}

	public void displayUnexpectedThrowable(UnexpectedThrowable unexpectedThrowable) {
		this.mcFrame.removeAll();
		this.mcFrame.add(new PanelCrashReport(unexpectedThrowable), "Center");
		this.mcFrame.validate();
	}
}
