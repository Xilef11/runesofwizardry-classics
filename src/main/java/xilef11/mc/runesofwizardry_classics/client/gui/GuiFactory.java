/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.client.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

/**
 * @author Xilef11
 *
 */
public class GuiFactory implements IModGuiFactory {

	/* (non-Javadoc)
	 * @see cpw.mods.fml.client.IModGuiFactory#initialize(net.minecraft.client.Minecraft)
	 */
	@Override
	public void initialize(Minecraft minecraftInstance) {
		//might not be used
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cpw.mods.fml.client.IModGuiFactory#mainConfigGuiClass()
	 */
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return ModGuiConfig.class;
	}

	/* (non-Javadoc)
	 * @see cpw.mods.fml.client.IModGuiFactory#runtimeGuiCategories()
	 */
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		//Might not be used
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see cpw.mods.fml.client.IModGuiFactory#getHandlerFor(cpw.mods.fml.client.IModGuiFactory.RuntimeOptionCategoryElement)
	 */
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(
			RuntimeOptionCategoryElement element) {
		//might not be used
		// TODO Auto-generated method stub
		return null;
	}

}
