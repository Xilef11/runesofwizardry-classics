package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import xilef11.mc.runesofwizardry_classics.utils.Utils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityEnchantingFortune extends RuneEntity {

	public RuneEntityEnchantingFortune(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			if(negated || Utils.takeXP(player, 15)){
				//find the bow stack
				ItemStack toEnchant=null;
				if(sacrifice!=null){
					for(ItemStack i:sacrifice){
						if(i.getItem()==Items.diamond_pickaxe || i.getItem()==Items.diamond_shovel){
							toEnchant=i;
						}
					}
				}
				if(toEnchant==null && negated)toEnchant=new ItemStack(Items.diamond_pickaxe);
				toEnchant.addEnchantment(Enchantment.fortune, Enchantment.fortune.getMaxLevel()+1);
				toEnchant.setItemDamage(0);
				Utils.spawnItemCentered(world, getPos(), toEnchant);
				this.onPatternBroken();
			}else{
				this.onPatternBrokenByPlayer(player);
			}
		}

	}

	@Override
	public void update() {

	}

}
