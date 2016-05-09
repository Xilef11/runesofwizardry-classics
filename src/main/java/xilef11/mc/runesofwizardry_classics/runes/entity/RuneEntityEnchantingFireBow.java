package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityEnchantingFireBow extends RuneEntity {

	public RuneEntityEnchantingFireBow(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			if(negated || Utils.takeXP(player, 5)){
				//find the bow stack
				ItemStack bow=null;
				if(sacrifice!=null){
					for(ItemStack i:sacrifice){
						if(i.getItem()==Items.BOW){
							bow=i;
						}
					}
				}
				if(bow==null && negated)bow = new ItemStack(Items.BOW);
				bow.addEnchantment(Enchantment.flame, Enchantment.flame.getMaxLevel());
				bow.setItemDamage(0);
				Utils.spawnItemCentered(world, getPos(), bow);
				this.onPatternBroken();
			}else{
				this.onPatternBrokenByPlayer(player);
			}
		}

	}

	@Override
	public void update() {
		// not much until we have FX

	}

}
