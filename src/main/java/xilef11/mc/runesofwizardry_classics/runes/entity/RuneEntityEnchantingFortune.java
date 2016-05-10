package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

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
				boolean sword=false;
				if(sacrifice!=null){
					for(ItemStack i:sacrifice){
						if(i.getItem()==Items.DIAMOND_PICKAXE){
							toEnchant=i;
							sword=false;
						}
						if(i.getItem()==Items.DIAMOND_SWORD){
							toEnchant=i;
							sword=true;
						}
					}
				}
				if(toEnchant==null && negated)toEnchant=new ItemStack(Items.DIAMOND_PICKAXE);
				if(!sword)toEnchant.addEnchantment(Enchantments.FORTUNE, Enchantments.FORTUNE.getMaxLevel()+1);
				else toEnchant.addEnchantment(Enchantments.LOOTING, Enchantments.LOOTING.getMaxLevel()+1);
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
