package net.minecraft.src;

public class GuiMultiplayer extends GuiScreen {
	private GuiScreen parentScreen;
	private int updateCounter = 0;
	private String ipText = "";

	public GuiMultiplayer(GuiScreen guiScreen1) {
		this.parentScreen = guiScreen1;
	}

	public void updateScreen() {
		++this.updateCounter;
	}

	public void initGui() {
		this.controlList.clear();
		this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, "Connect"));
		this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
		((GuiButton)this.controlList.get(0)).enabled = false;
	}

	protected void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 1) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else if(button.id == 0) {
				String[] string2 = this.ipText.split(":");
				this.mc.displayGuiScreen(new GuiConnecting(this.mc, string2[0], string2.length > 1 ? Integer.parseInt(string2[1]) : 25565));
			}

		}
	}

	protected void keyTyped(char character, int key) {
		if(character == 22) {
			String string3 = GuiScreen.getClipboardString();
			if(string3 == null) {
				string3 = "";
			}

			int i4 = 32 - this.ipText.length();
			if(i4 > string3.length()) {
				i4 = string3.length();
			}

			if(i4 > 0) {
				this.ipText = this.ipText + string3.substring(0, i4);
			}
		}

		if(character == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

		if(key == 14 && this.ipText.length() > 0) {
			this.ipText = this.ipText.substring(0, this.ipText.length() - 1);
		}

		if(" !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_\'abcdefghijklmnopqrstuvwxyz{|}~\u2302\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb".indexOf(character) >= 0 && this.ipText.length() < 32) {
			this.ipText = this.ipText + character;
		}

		((GuiButton)this.controlList.get(0)).enabled = this.ipText.length() > 0;
	}

	public void drawScreen(int mouseX, int mouseY, float renderPartialTick) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "Play Multiplayer", this.width / 2, this.height / 4 - 60 + 20, 0xFFFFFF);
		this.drawString(this.fontRenderer, "Enter the IP of a server to connect to it:", this.width / 2 - 105, this.height / 4 - 60 + 60 + 0, 10526880);
		int i4 = this.width / 2 - 100;
		int i5 = this.height / 4 - 10 + 50 + 18;
		short s6 = 200;
		byte b7 = 20;
		this.drawRect(i4 - 1, i5 - 1, i4 + s6 + 1, i5 + b7 + 1, -6250336);
		this.drawRect(i4, i5, i4 + s6, i5 + b7, 0xFF000000);
		this.drawString(this.fontRenderer, this.ipText + (this.updateCounter / 6 % 2 == 0 ? "_" : ""), i4 + 4, i5 + (b7 - 8) / 2, 14737632);
		super.drawScreen(mouseX, mouseY, renderPartialTick);
	}
}
