package xilef11.mc.runesofwizardry_classics.proxy;

import net.minecraft.item.Item;

public interface IProxy {
	public void RegisterItemModel(Item item,int meta,String modelpath,String variant);
}
