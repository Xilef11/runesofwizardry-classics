package xilef11.mc.runesofwizardry_classics;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Refs.MODID, version = Refs.VERSION, name=Refs.NAME, dependencies = "before:runesofwizardry")
public class RunesofWizardry_Classics
{
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		System.out.println("classics in preinit");
	}
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
    }
}
