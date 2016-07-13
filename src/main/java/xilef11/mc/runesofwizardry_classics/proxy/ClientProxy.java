package xilef11.mc.runesofwizardry_classics.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy implements IProxy{

	@Override
	public void RegisterItemModel(Item item, int meta, String modelpath,String variant) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modelpath, variant));
	}

}
