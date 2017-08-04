package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Collection;
import java.util.Set;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.RunesofWizardry_Classics;
import xilef11.mc.runesofwizardry_classics.managers.IVoidStorageCapability;
import xilef11.mc.runesofwizardry_classics.managers.VoidStorageCapability;
import xilef11.mc.runesofwizardry_classics.utils.Utils;
public class RuneEntityVoid extends RuneEntity {
	public RuneEntityVoid(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		World world = player.world;
		if(!world.isRemote){
			if(!(negated || Utils.takeXP(player, 3))){
				this.onPatternBrokenByPlayer(player);
				return;
			}
			if(sacrifice!=null){
				//Store the items
				IVoidStorageCapability storage = player.getCapability(VoidStorageCapability.VOID_STORAGE_CAPABILITY, null);
				if(storage!=null){//in case something is wrong
					for(ItemStack i:sacrifice){
						storage.addStackToVoid(i);
					}
					RunesofWizardry_Classics.log().debug("Stored sacrifice in the void");
				}else{
					RunesofWizardry_Classics.log().error("player did not have VOID_STORAGE capability");
				}
			}else{
				IVoidStorageCapability storage = player.getCapability(VoidStorageCapability.VOID_STORAGE_CAPABILITY, null);
				if(storage!=null){//in case something is wrong
					Collection<ItemStack> inv = storage.getVoidInventory();
					for(ItemStack i:inv){
						Utils.spawnItemCentered(world, getPos(), i);
					}
					inv.clear();
					RunesofWizardry_Classics.log().debug("Spawned void inventory");
				}else{
					RunesofWizardry_Classics.log().error("player did not have VOID_STORAGE capability");
				}
			}
			this.onPatternBroken();
		}
	}
	@Override
	public void update() {
		if(entity.ticksExisted()==1*Refs.TPS)this.renderActive=false;
	}
}
