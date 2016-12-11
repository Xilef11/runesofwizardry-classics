package xilef11.mc.runesofwizardry_classics.integration.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.ItemSpiritPickaxe;
import xilef11.mc.runesofwizardry_classics.items.ItemSpiritSword;

@JEIPlugin
public class RunesofWizardry_ClassicsPlugin implements IModPlugin{

	@Override
	public void register(IModRegistry registry) {
		registry.addDescription(ItemSpiritSword.createStack(), Refs.Lang.Jei.SPIRIT_SWORD);
		registry.addDescription(new ItemStack(ItemSpiritPickaxe.instance()), Refs.Lang.Jei.SPIRIT_PICKAXE);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		
	}

}
