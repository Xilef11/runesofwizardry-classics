package xilef11.mc.runesofwizardry_classics;

import net.minecraftforge.common.MinecraftForge;import net.minecraftforge.fml.common.Mod;import net.minecraftforge.fml.common.Mod.EventHandler;import net.minecraftforge.fml.common.event.FMLInitializationEvent;import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;import xilef11.mc.runesofwizardry_classics.items.ClassicDusts;import xilef11.mc.runesofwizardry_classics.items.DustVariable;import com.zpig333.runesofwizardry.api.DustRegistry;
//note: loading before a dependency causes a noclassdeffound outside of a dev environment
@Mod(modid = Refs.MODID, version = Refs.VERSION, name=Refs.NAME, guiFactory=Refs.GUI_FACTORY, dependencies = "required-after:runesofwizardry@[1.8.9-0.6.0,)")
//@Mod(modid = Refs.MODID, version = Refs.VERSION, name=Refs.NAME, guiFactory=Refs.GUI_FACTORY, dependencies = "required-after:runesofwizardry")
public class RunesofWizardry_Classics
{
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		ModLogger.logInfo("Registering Classic dusts");
		ClassicDusts.instance = new ClassicDusts();
		DustRegistry.registerDust(ClassicDusts.instance);
		DustVariable.instance=new DustVariable();
		DustRegistry.registerDust(DustVariable.instance);
		//init the runes
		ModRunes.initRunes();
		//register the config
		Config.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new Config());
	}
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	ModRecipes.registerDustRecipes();
    	//register the runes
    	ModRunes.registerRunes();
    }
}
