package xilef11.mc.runesofwizardry_classics.managers;

import java.util.Collection;import net.minecraft.item.ItemStack;

public interface IVoidStorageCapability {

	public Collection<ItemStack> getVoidInventory();

	public void addStackToVoid(ItemStack stack);

}
