package xilef11.mc.runesofwizardry_classics;
import org.apache.logging.log4j.Logger;

import com.zpig333.runesofwizardry.api.DustRegistry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xilef11.mc.runesofwizardry_classics.integration.guideapi.GuideBookAdditions;
import xilef11.mc.runesofwizardry_classics.items.ClassicDusts;
import xilef11.mc.runesofwizardry_classics.items.DustVariable;
import xilef11.mc.runesofwizardry_classics.proxy.IProxy;
//note: loading before a dependency causes a noclassdeffound outside of a dev environment
//@Mod(modid = Refs.MODID, version = Refs.VERSION, name=Refs.NAME, guiFactory=Refs.GUI_FACTORY, dependencies = "required-after:runesofwizardry@[1.12-0.8.3,)",acceptedMinecraftVersions = "[1.2,1.13)")
@Mod(modid = Refs.MODID, version = Refs.VERSION, name=Refs.NAME, guiFactory=Refs.GUI_FACTORY, dependencies = "required-after:runesofwizardry;after:guideapi")
public class RunesofWizardry_Classics
{
	@SidedProxy(clientSide=Refs.CLIENT_PROXY,serverSide=Refs.SERVER_PROXY)
	public static IProxy proxy;
	
	private static Logger logger;
	/**Returns the logger for the mod**/
	public static Logger log(){return logger;}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		logger = event.getModLog();
		RunesofWizardry_Classics.log().info("Registering Classic dusts");
		ClassicDusts.instance = new ClassicDusts();
		DustRegistry.registerDust(ClassicDusts.instance);
		DustVariable.instance=new DustVariable();
		DustRegistry.registerDust(DustVariable.instance);
		//init the runes
		ModRunes.initRunes();
		ModInscriptions.initInscriptions();
		//register the config
		Config.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new Config());
	}
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	//register the runes
    	ModRunes.registerRunes();
    	ModInscriptions.registerInscriptions();
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
    	if(Loader.isModLoaded("guideapi")){
    		GuideBookAdditions.handlePost();
    	}
    }
}
