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
public class RuneEntityEnchantingSilktouch extends RuneEntity {
	public RuneEntityEnchantingSilktouch(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		World world = player.world;
		if(!world.isRemote){
			if(negated || Utils.takeXP(player, 10)){
				//find the bow stack
				ItemStack toEnchant=ItemStack.EMPTY;
				if(sacrifice!=null){
					for(ItemStack i:sacrifice){
						if(i.getItem()==Items.DIAMOND_PICKAXE || i.getItem()==Items.DIAMOND_SHOVEL){
							toEnchant=i;
						}
					}
				}
				if(toEnchant.isEmpty() && negated)toEnchant=new ItemStack(Items.DIAMOND_PICKAXE);
				toEnchant.addEnchantment(Enchantments.SILK_TOUCH, Enchantments.SILK_TOUCH.getMaxLevel());
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
		//not much until FX
	}
}
