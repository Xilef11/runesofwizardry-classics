/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import xilef11.mc.runesofwizardry_classics.Config;
import xilef11.mc.runesofwizardry_classics.Refs;

/**
 * @author Xilef11
 *
 */
public class ModGuiConfig extends GuiConfig {

	public ModGuiConfig(GuiScreen screen) {
		//FIXME child elements is empty w/ custom category
		super(screen, new ConfigElement(
				Config.config.getCategory(Config.CATEGORY_RUNES)).getChildElements(),
				Refs.MODID, false, false,
		GuiConfig.getAbridgedConfigPath(Config.config.toString()));

	}

}