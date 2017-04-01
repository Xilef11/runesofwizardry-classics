package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.items.EnumDustTypes;
import xilef11.mc.runesofwizardry_classics.runes.RuneHealing;
import xilef11.mc.runesofwizardry_classics.utils.Utils;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
public class RuneEntityHealing extends RuneEntity {
	public RuneEntityHealing(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity, RuneHealing creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,
			ItemStack[] sacrifice, boolean negated) {
		World world = player.world;
		System.out.println("WTF");
		if(!world.isRemote){
			if(!(negated || Utils.takeXP(player, 2))){
				this.onPatternBrokenByPlayer(player);
				return;
			}
			Coords var = ((RuneHealing)creator).getVariableDusts().iterator().next();
			int duration,power,food;
			float sat;
			switch(EnumDustTypes.getByMeta(placedPattern[var.row][var.col].getMetadata())){
			case PLANT: duration=4;power=1;food=0;sat=0;//gives regen 1
				break;
			case GUNPOWDER: duration=5;power=2;food=0;sat=0;//gives regen 2
				break;
			case LAPIS: duration=10;power=2;food=5;sat=0.6F;
				break;
			case BLAZE: duration=32;power=4;food=8;sat=0.8F;//gives regen 4 (regen 5 just gives regen 1)
				break;
			default: duration=power=0;food=0;sat=0;
				break;
			}
			List<EntityLivingBase> ents = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(getPos().add(-3, -1, -3), getPos().add(3,1,3)));
			for(EntityLivingBase e:ents){
				e.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, duration*20, power-1));
				if(e instanceof EntityPlayer){
					EntityPlayer p = (EntityPlayer)e;
					p.getFoodStats().addStats(food, sat);
				}
			}
		}
	}
	@Override
	public void update() {
			if(entity.ticksExisted()>=100){
				this.onPatternBroken();
			}
	}
}
