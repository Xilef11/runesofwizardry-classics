/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

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
		World world = player.worldObj;
		//TODO FX
		if(!world.isRemote){
			//grab the list of records
			Map<String,ItemRecord> recordMap = ReflectionHelper.getPrivateValue(ItemRecord.class, (ItemRecord)Items.record_13, "RECORDS","field_150928_b");//XXX recheck name when update
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
			this.onPatternBroken();//deactivate
		}

	}

	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
	}

}
