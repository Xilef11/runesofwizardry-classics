package xilef11.mc.runesofwizardry_classics;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xilef11.mc.runesofwizardry_classics.items.ClassicDusts;

import com.zpig333.runesofwizardry.api.DustRegistry;

@Mod(modid = Refs.MODID, version = Refs.VERSION, name=Refs.NAME, dependencies = "before:runesofwizardry")
public class RunesofWizardry_Classics
{
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		ModLogger.logInfo("Registering Classic dusts");
		ClassicDusts.instance = new ClassicDusts();
		DustRegistry.registerDust(ClassicDusts.instance);
	}
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	ModRecipes.registerDustRecipes();
    }
}
