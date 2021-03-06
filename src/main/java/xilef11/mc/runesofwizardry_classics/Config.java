package xilef11.mc.runesofwizardry_classics;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xilef11.mc.runesofwizardry_classics.inscriptions.ClassicInscription;
import xilef11.mc.runesofwizardry_classics.runes.ClassicRune;
/**
 * This class handles the config file for the mod.
 * @author Xilef11
 *
 */
public class Config {
	public static Configuration config;
	public static void init(File configFile){
		RunesofWizardry_Classics.log().info("Initialising config");
		if(config==null){
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}
	@SubscribeEvent
	public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event){
		if(event.getModID().equals(Refs.MODID)){
			//resync configs
			loadConfiguration();
		}
	}
	public static final String CATEGORY_PERMISSIONS="Runes";
	public static final String CATEGORY_BEHAVIOUR="Rune Behaviour";
	public static final String CATEGORY_DEBUG="zDebug";
	public static final String PERMISSIONS_ALL="ALL", PERMISSIONS_OP="OP", PERMISSIONS_NONE="NONE";
	private static final String CATEGORY_PERMISSIONS_INSC = "Inscriptions";
	//map of rune to permissions
	private static Map<String,String> runePerms = new HashMap<>();
	private static Map<String,String> inscPerms = new HashMap<>();
	public static String getPermissionsForRune(ClassicRune rune){
		return runePerms.get(rune.getID());
	}
	public static String getPermissionsForInscription(ClassicInscription insc){
		return inscPerms.get(insc.getID());
	}
	/** Mode for generating the item-> entity map for rune of resurrection**/
	public static String resurrectionMode;
	/** does the rune of locked time stop blocks from falling**/
	public static boolean lockedTimeFalling=true;
	/**blacklist for rune of level earth breaking**/
	public static List<String> levelEarthBlackList;
	public static List<String> farmReplaceable;
	
	//DEBUG: disables rune of resurrection
	public static boolean disableResurrection=false;
	//disable decorative/WIP runes?
	public static boolean disableWIP=false;
	public static boolean cacheRuneInfo=true;
	//always show coordinates on return inscription tooltip
	public static boolean returnCoords;
	//radius for the inscription of foresight
	public static int foresightRadius;
	//allow old inscription enchanting rune
	public static boolean oldInscriptionEnchant;
	
	private static void loadConfiguration(){
		//read properties
		config.setCategoryComment(CATEGORY_PERMISSIONS, "Manage who can activate a rune. valid values: ALL, OP, NONE");
		config.setCategoryRequiresWorldRestart(CATEGORY_PERMISSIONS, false);
		config.setCategoryLanguageKey(CATEGORY_PERMISSIONS, Refs.Lang.CONFIG+".catruneperms");
		for(ClassicRune rune:ModRunes.getRunes()){
			String id = rune.getID();
			String perms = config.getString(id, CATEGORY_PERMISSIONS, PERMISSIONS_ALL, rune.getName(), new String[]{PERMISSIONS_ALL,PERMISSIONS_OP,PERMISSIONS_NONE},rune.getName());
			runePerms.put(id,perms);
		}
		config.setCategoryComment(CATEGORY_PERMISSIONS_INSC, "Manage who can charge an inscription. valid values: ALL, OP, NONE");
		config.setCategoryRequiresWorldRestart(CATEGORY_PERMISSIONS_INSC, false);
		config.setCategoryLanguageKey(CATEGORY_PERMISSIONS_INSC, Refs.Lang.CONFIG+".catinscperms");
		for(ClassicInscription insc:ModInscriptions.getInscriptions()){
			String id = insc.getID();
			String perms = config.getString(id, CATEGORY_PERMISSIONS_INSC, PERMISSIONS_ALL, insc.getName(), new String[]{PERMISSIONS_ALL,PERMISSIONS_OP,PERMISSIONS_NONE},insc.getName());
			inscPerms.put(id,perms);
		}
		//behaviour
		resurrectionMode = config.getString("runeResurrection mode", CATEGORY_BEHAVIOUR, "JSON", "Sets the method used to build the lookup table for the Rune of Resurrection. Valid values: [ LootTables , JSON , Kill ]. JSON will create the file (using the LootTable method) if it doesn't exist", new String[]{"LootTables","JSON","Kill"});
		lockedTimeFalling = config.getBoolean("lockedTime falling blocks", CATEGORY_BEHAVIOUR, true, "Does the Rune of Locked Time prevent blocks from falling?");
		levelEarthBlackList = Arrays.asList(config.getStringList("levelEarth blacklist", CATEGORY_BEHAVIOUR, new String[0], "Blocks in this list will not be broken by the Rune of Level Earth or Rune of the Depths."));
		farmReplaceable = Arrays.asList(config.getStringList("farmland replaceable", CATEGORY_BEHAVIOUR, new String[]{"minecraft:dirt","minecraft:grass","minecraft:sand","minecraft:farmland"}, "Blocks in this list may be replaced with farmland by the Rune of te Farm"));
		
		config.setCategoryRequiresMcRestart(CATEGORY_DEBUG, true);
		disableResurrection=config.getBoolean("disableResurrection", CATEGORY_DEBUG, false, "Completely disables the rune of resurrection. set to true in case of crashes");
		disableWIP=config.getBoolean("disableDecorative", CATEGORY_DEBUG, false, "Completely disables decorative/WIP runes");
		cacheRuneInfo=config.getBoolean("cacheRuneInfo", CATEGORY_DEBUG, true, "Should the patterns and sacrifices be kept in memory after loading. if false, they will be reloaded every time the rune is activated");
		
		returnCoords=config.getBoolean("displayReturnCoords", Configuration.CATEGORY_CLIENT, false, "If enabled, coordinates will always be shown in the Return inscription tooltip");
		
		foresightRadius=config.getInt("foresightRadius", Configuration.CATEGORY_CLIENT, 5, 0, 32, "Radius for the Inscription of Foresight");
		
		oldInscriptionEnchant=config.getBoolean("oldInscriptionEnchant", Configuration.CATEGORY_GENERAL, true, "Enable the Classic Inscription Enchanting Rune");
		
		if(config.hasChanged()){
			//RunesofWizardry_Classics.log().info("Config has changed");
			config.save();
		}
	}
}
