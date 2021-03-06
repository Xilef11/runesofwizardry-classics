/**
 *
 */
package xilef11.mc.runesofwizardry_classics;
/**
 * @author Xilef11
 *
 */
public final class Refs {
	private Refs(){}
	public static final String MODID = "runesofwizardry_classics";
	public static final String VERSION = "@VERSION@";
	public static final String NAME="Runes of Wizardry - Classic Dusts Pack";
	public static final String GUI_FACTORY="xilef11.mc.runesofwizardry_classics.client.gui.GuiFactory";
	public static final String CLIENT_PROXY = "xilef11.mc.runesofwizardry_classics.proxy.ClientProxy",
							   SERVER_PROXY="xilef11.mc.runesofwizardry_classics.proxy.ServerProxy";
	public static final String PATTERN_PATH=MODID+":patterns/";
	public static final String TEXTURE_PATH=MODID+":";
	public static final int TICKS_PER_DAY=24000;
	public static final int TPS=20;
	public static final class Lang{
		private Lang(){}
		public static final String RUNE=MODID+".rune";
		public static final String CONFIG=MODID+".configuration";
		public static final String DEC=RUNE+".decorative";
		
		public static final class Jei{
			private Jei(){}
			public static final String JEI=MODID+".jei";
			public static final String SPIRIT_SWORD=JEI+".spirit_sword";
			public static final String SPIRIT_PICKAXE=JEI+".spirit_pickaxe";
		}
	}
}
