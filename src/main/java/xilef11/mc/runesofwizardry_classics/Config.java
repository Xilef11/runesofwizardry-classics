package xilef11.mc.runesofwizardry_classics;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xilef11.mc.runesofwizardry_classics.runes.ClassicRune;

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
	public static final String CATEGORY_RUNES="Runes";
	public static final String PERMISSIONS_ALL="ALL", PERMISSIONS_OP="OP", PERMISSIONS_NONE="NONE";
	//map of rune to permissions
	private static Map<String,String> runePerms = new HashMap<String, String>();
	public static String getPermissionsForRune(ClassicRune rune){
		return runePerms.get(rune);
	}
	private static void loadConfiguration(){
		//read properties
		config.setCategoryComment(CATEGORY_RUNES, "Manage who can activate a rune. valid values: ALL, OP, NONE");
		config.setCategoryRequiresWorldRestart(CATEGORY_RUNES, false);
		config.setCategoryLanguageKey(CATEGORY_RUNES, Refs.Lang.CONFIG+".catruneperms");
		for(ClassicRune rune:ModRunes.getRunes()){
			String id = rune.createRune(new ItemStack[][]{},EnumFacing.NORTH, null, null).getRuneID();
			String perms = config.getString(id, CATEGORY_RUNES, PERMISSIONS_ALL, StatCollector.translateToLocal(rune.getName()), new String[]{PERMISSIONS_ALL,PERMISSIONS_OP,PERMISSIONS_NONE},rune.getName());
			runePerms.put(id,perms);
		}
		
		if(config.hasChanged()){
			//ModLogger.logInfo("Config has changed");
			config.save();
		}

	}
}
