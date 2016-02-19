package xilef11.mc.runesofwizardry_classics;

import com.zpig333.runesofwizardry.api.DustRegistry;
import com.zpig333.runesofwizardry.api.IDust;

import xilef11.mc.runesofwizardry_classics.items.ClassicDusts;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Refs.MODID, version = Refs.VERSION, name=Refs.NAME, dependencies = "before:runesofwizardry")
public class RunesofWizardry_Classics
{
	public IDust classic;
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		ModLogger.logInfo("Registering Classic dusts");
		classic = new ClassicDusts();
		DustRegistry.registerDust(classic);
		System.out.println("classics finished preinit");
	}
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
}
