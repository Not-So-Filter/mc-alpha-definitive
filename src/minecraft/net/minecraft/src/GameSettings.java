package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

public class GameSettings {
	private static final String[] RENDER_DISTANCES = new String[]{"FAR", "NORMAL", "SHORT", "TINY"};
	private static final String[] DIFFICULTY_LEVELS = new String[]{"Peaceful", "Easy", "Normal", "Hard"};
	public float musicVolume = 1.0F;
	public float soundVolume = 1.0F;
	public float mouseSensitivity = 0.5F;
	public boolean invertMouse = false;
	public int renderDistance = 0;
	public boolean viewBobbing = true;
	public boolean anaglyph = false;
	public boolean limitFramerate = false;
	public boolean fancyGraphics = true;
	public KeyBinding keyBindForward = new KeyBinding("Forward", Keyboard.KEY_W);
	public KeyBinding keyBindLeft = new KeyBinding("Left", Keyboard.KEY_A);
	public KeyBinding keyBindBack = new KeyBinding("Back", Keyboard.KEY_S);
	public KeyBinding keyBindRight = new KeyBinding("Right", Keyboard.KEY_D);
	public KeyBinding keyBindJump = new KeyBinding("Jump", Keyboard.KEY_SPACE);
	public KeyBinding keyBindInventory = new KeyBinding("Inventory", Keyboard.KEY_I);
	public KeyBinding keyBindDrop = new KeyBinding("Drop", Keyboard.KEY_Q);
	public KeyBinding keyBindChat = new KeyBinding("Chat", Keyboard.KEY_T);
	public KeyBinding keyBindToggleFog = new KeyBinding("Toggle fog", Keyboard.KEY_F);
	public KeyBinding keyBindSneak = new KeyBinding("Sneak", Keyboard.KEY_LSHIFT);
	public KeyBinding[] keyBindings = new KeyBinding[]{this.keyBindForward, this.keyBindLeft, this.keyBindBack, this.keyBindRight, this.keyBindJump, this.keyBindSneak, this.keyBindDrop, this.keyBindInventory, this.keyBindChat, this.keyBindToggleFog};
	protected Minecraft mc;
	private File optionsFile;
	public int numberOfOptions = 10;
	public int difficulty = 2;
	public boolean thirdPersonView = false;

	public GameSettings(Minecraft minecraft, File file) {
		this.mc = minecraft;
		this.optionsFile = new File(file, "options.txt");
		this.loadOptions();
	}

	public GameSettings() {
	}

	public String getKeyBindingDescription(int i1) {
		return this.keyBindings[i1].keyDescription + ": " + Keyboard.getKeyName(this.keyBindings[i1].keyCode);
	}

	public void setKeyBinding(int i1, int i2) {
		this.keyBindings[i1].keyCode = i2;
		this.saveOptions();
	}

	public void setOptionFloatValue(int i1, float f2) {
		if(i1 == 0) {
			this.musicVolume = f2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(i1 == 1) {
			this.soundVolume = f2;
			this.mc.sndManager.onSoundOptionsChanged();
		}

		if(i1 == 3) {
			this.mouseSensitivity = f2;
		}

	}

	public void setOptionValue(int i1, int i2) {
		if(i1 == 2) {
			this.invertMouse = !this.invertMouse;
		}

		if(i1 == 4) {
			this.renderDistance = this.renderDistance + i2 & 3;
		}

		if(i1 == 5) {
			this.viewBobbing = !this.viewBobbing;
		}

		if(i1 == 6) {
			this.anaglyph = !this.anaglyph;
			this.mc.renderEngine.refreshTextures();
		}

		if(i1 == 7) {
			this.limitFramerate = !this.limitFramerate;
		}

		if(i1 == 8) {
			this.difficulty = this.difficulty + i2 & 3;
		}

		if(i1 == 9) {
			this.fancyGraphics = !this.fancyGraphics;
			this.mc.renderGlobal.loadRenderers();
		}

		this.saveOptions();
	}

	public int isSlider(int i1) {
		return i1 == 0 ? 1 : (i1 == 1 ? 1 : (i1 == 3 ? 1 : 0));
	}

	public float getOptionFloatValue(int i1) {
		return i1 == 0 ? this.musicVolume : (i1 == 1 ? this.soundVolume : (i1 == 3 ? this.mouseSensitivity : 0.0F));
	}

	public String getOptionDisplayString(int i1) {
		return i1 == 0 ? "Music: " + (this.musicVolume > 0.0F ? (int)(this.musicVolume * 100.0F) + "%" : "OFF") : (i1 == 1 ? "Sound: " + (this.soundVolume > 0.0F ? (int)(this.soundVolume * 100.0F) + "%" : "OFF") : (i1 == 2 ? "Invert mouse: " + (this.invertMouse ? "ON" : "OFF") : (i1 == 3 ? (this.mouseSensitivity == 0.0F ? "Sensitivity: *yawn*" : (this.mouseSensitivity == 1.0F ? "Sensitivity: HYPERSPEED!!!" : "Sensitivity: " + (int)(this.mouseSensitivity * 200.0F) + "%")) : (i1 == 4 ? "Render distance: " + RENDER_DISTANCES[this.renderDistance] : (i1 == 5 ? "View bobbing: " + (this.viewBobbing ? "ON" : "OFF") : (i1 == 6 ? "3d anaglyph: " + (this.anaglyph ? "ON" : "OFF") : (i1 == 7 ? "Limit framerate: " + (this.limitFramerate ? "ON" : "OFF") : (i1 == 8 ? "Difficulty: " + DIFFICULTY_LEVELS[this.difficulty] : (i1 == 9 ? "Graphics: " + (this.fancyGraphics ? "FANCY" : "FAST") : "")))))))));
	}

	public void loadOptions() {
		try {
			if(!this.optionsFile.exists()) {
				return;
			}

			BufferedReader bufferedReader1 = new BufferedReader(new FileReader(this.optionsFile));
			String string2 = "";

			while((string2 = bufferedReader1.readLine()) != null) {
				String[] string3 = string2.split(":");
				if(string3[0].equals("music")) {
					this.musicVolume = this.parseFloat(string3[1]);
				}

				if(string3[0].equals("sound")) {
					this.soundVolume = this.parseFloat(string3[1]);
				}

				if(string3[0].equals("mouseSensitivity")) {
					this.mouseSensitivity = this.parseFloat(string3[1]);
				}

				if(string3[0].equals("invertYMouse")) {
					this.invertMouse = string3[1].equals("true");
				}

				if(string3[0].equals("viewDistance")) {
					this.renderDistance = Integer.parseInt(string3[1]);
				}

				if(string3[0].equals("bobView")) {
					this.viewBobbing = string3[1].equals("true");
				}

				if(string3[0].equals("anaglyph3d")) {
					this.anaglyph = string3[1].equals("true");
				}

				if(string3[0].equals("limitFramerate")) {
					this.limitFramerate = string3[1].equals("true");
				}

				if(string3[0].equals("difficulty")) {
					this.difficulty = Integer.parseInt(string3[1]);
				}

				if(string3[0].equals("fancyGraphics")) {
					this.fancyGraphics = string3[1].equals("true");
				}

				for(int i4 = 0; i4 < this.keyBindings.length; ++i4) {
					if(string3[0].equals("key_" + this.keyBindings[i4].keyDescription)) {
						this.keyBindings[i4].keyCode = Integer.parseInt(string3[1]);
					}
				}
			}

			bufferedReader1.close();
		} catch (Exception exception5) {
			System.out.println("Failed to load options");
			exception5.printStackTrace();
		}

	}

	private float parseFloat(String string1) {
		return string1.equals("true") ? 1.0F : (string1.equals("false") ? 0.0F : Float.parseFloat(string1));
	}

	public void saveOptions() {
		try {
			PrintWriter printWriter1 = new PrintWriter(new FileWriter(this.optionsFile));
			printWriter1.println("music:" + this.musicVolume);
			printWriter1.println("sound:" + this.soundVolume);
			printWriter1.println("invertYMouse:" + this.invertMouse);
			printWriter1.println("mouseSensitivity:" + this.mouseSensitivity);
			printWriter1.println("viewDistance:" + this.renderDistance);
			printWriter1.println("bobView:" + this.viewBobbing);
			printWriter1.println("anaglyph3d:" + this.anaglyph);
			printWriter1.println("limitFramerate:" + this.limitFramerate);
			printWriter1.println("difficulty:" + this.difficulty);
			printWriter1.println("fancyGraphics:" + this.fancyGraphics);

			for(int i2 = 0; i2 < this.keyBindings.length; ++i2) {
				printWriter1.println("key_" + this.keyBindings[i2].keyDescription + ":" + this.keyBindings[i2].keyCode);
			}

			printWriter1.close();
		} catch (Exception exception3) {
			System.out.println("Failed to save options");
			exception3.printStackTrace();
		}

	}
}
