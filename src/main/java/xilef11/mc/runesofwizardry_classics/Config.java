package xilef11.mc.runesofwizardry_classics;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This class handles the config file for the mod.
 * @author Xilef11
 *
 */
public class Config {
	public static Configuration config;
	
	public static void init(File configFile){
		ModLogger.logInfo("Initialising config");
		if(config==null){
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}

	@SubscribeEvent
	public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if(event.modID.equals(Refs.MODID)){
			//resync configs
			loadConfiguration();
		}
	}
	public static final String CATEGORY_RUNES="runes";
	private static void loadConfiguration(){
		//read properties
		config.setCategoryComment(CATEGORY_RUNES, "Enable or disable specific runes");
		config.getBoolean("test", CATEGORY_RUNES, true, "This is a test");
		
		
		if(config.hasChanged()){
			//ModLogger.logInfo("Config has changed");
			config.save();
		}

	}
}
