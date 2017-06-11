package xilef11.mc.runesofwizardry_classics.integration.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.ItemSpiritPickaxe;
import xilef11.mc.runesofwizardry_classics.items.ItemSpiritSword;

@JEIPlugin
public class RunesofWizardry_ClassicsPlugin implements IModPlugin{

	@Override
	public void register(IModRegistry registry) {
		registry.addIngredientInfo(ItemSpiritSword.createStack(),ItemStack.class, Refs.Lang.Jei.SPIRIT_SWORD);
		registry.addIngredientInfo(new ItemStack(ItemSpiritPickaxe.instance()),ItemStack.class, Refs.Lang.Jei.SPIRIT_PICKAXE);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {
		
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
	}

}
