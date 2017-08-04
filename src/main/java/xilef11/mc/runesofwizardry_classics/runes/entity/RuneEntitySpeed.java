package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Set;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneSpeed;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;
public class RuneEntitySpeed extends RuneEntity {
	public RuneEntitySpeed(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneSpeed creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		if(!player.world.isRemote){
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
			player.addPotionEffect(new PotionEffect(MobEffects.SPEED, duration, power));
		}
	}
	@Override
	public void update() {
		if(entity.ticksExisted()==2*Refs.TPS && !entity.getWorld().isRemote)this.onPatternBroken();
	}
}
