package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneSpeed;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntitySpeed extends RuneEntity {

	public RuneEntitySpeed(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneSpeed creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		if(!player.worldObj.isRemote){
			Coords p = ((RuneSpeed)creator).getVariableDusts().iterator().next();
			ItemStack stack = placedPattern[p.row][p.col];
			int power,duration;
			switch(EnumDustTypes.getByMeta(stack.getMetadata())){
			case PLANT: power=1 ; duration=20*30;
				break;
			case GUNPOWDER: power=1 ; duration=20*60;
				break;
			case LAPIS: power=2 ; duration=20*120;
				break;
			case BLAZE: power=4 ; duration=20*180;
				break;
			default: power=duration=0;
				break;
			}
			//the original was activatable by redstone and gave the effect to all entityliving on the rune
			player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, duration, power));
			this.onPatternBroken();
		}

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
