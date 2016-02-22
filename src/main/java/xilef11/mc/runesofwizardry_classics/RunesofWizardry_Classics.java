package xilef11.mc.runesofwizardry_classics;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xilef11.mc.runesofwizardry_classics.items.ClassicDusts;
import xilef11.mc.runesofwizardry_classics.items.DustVariable;

import com.zpig333.runesofwizardry.api.DustRegistry;

@Mod(modid = Refs.MODID, version = Refs.VERSION, name=Refs.NAME, guiFactory=Refs.GUI_FACTORY, dependencies = "before:runesofwizardry")
public class RunesofWizardry_Classics
{
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		//register the config
		Config.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new Config());
		
		ModLogger.logInfo("Registering Classic dusts");
		ClassicDusts.instance = new ClassicDusts();
		DustRegistry.registerDust(ClassicDusts.instance);
		DustVariable.instance=new DustVariable();
		DustRegistry.registerDust(DustVariable.instance);
	}
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	ModRecipes.registerDustRecipes();
    	//register the runes
    }
}
