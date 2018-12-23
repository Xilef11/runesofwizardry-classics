/**
 *
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;
/**
 * @author Xilef11
 *
 */
public class RuneEntityMusic extends RuneEntity {
	/**
	 * @param actualPattern
	 * @param facing
	 * @param dusts
	 * @param entity
	 */
	public RuneEntityMusic(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity,IRune creator) {
		super(actualPattern, facing, dusts, entity,creator);
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
		World world = entity.getWorld();
		if(!world.isRemote && entity.ticksExisted()==Refs.TPS*5){
			//grab the list of records
			//Map<String,ItemRecord> recordMap = ReflectionHelper.getPrivateValue(ItemRecord.class, (ItemRecord)Items.RECORD_13, "RECORDS","field_150928_b");
			Map<String,ItemRecord> recordMap = ObfuscationReflectionHelper.getPrivateValue(ItemRecord.class, (ItemRecord)Items.RECORD_13,"field_150928_b");
			Collection<ItemRecord> records = recordMap.values();
			//select a random number
			Random rand = new Random();
			int idx = rand.nextInt(records.size());
			//grab the record that matches our random number
			Iterator<ItemRecord> it = records.iterator();
			ItemRecord current=it.next();
			for(int i=1;i<idx;i++){
				current = it.next();
			}
			//spawn the record
			Utils.spawnItemCentered(world, getPos(), new ItemStack(current));
			//world.playSoundEffect(getPos().getX(), getPos().getY(), getPos().getZ(), "mob.chicken.plop", 0.5F, 0.8F + (world.rand.nextFloat() - world.rand.nextFloat()));
			world.playSound(null,getPos().getX(), getPos().getY(), getPos().getZ(), SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 0.5F, 0.8F + (world.rand.nextFloat() - world.rand.nextFloat()));
			this.onPatternBroken();//deactivate
		}
	}
}
