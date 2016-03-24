package xilef11.mc.runesofwizardry_classics.runes.entity;

import java.util.Set;

import xilef11.mc.runesofwizardry_classics.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;

public class RuneEntityCompression extends RuneEntity {

	public RuneEntityCompression(ItemStack[][] actualPattern,
			EnumFacing facing, Set<BlockPos> dusts,
			TileEntityDustActive entity, IRune creator) {
		super(actualPattern, facing, dusts, entity, creator);
	}

	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			int numCoal=0;
			for(ItemStack i:sacrifice){
				if(i.getItem()==Items.coal)numCoal+=i.stackSize;
			}
			if(negated && numCoal==0)numCoal=32;
			int numDiamonds = numCoal/32;
			while(numDiamonds>64){
				Utils.spawnItemCentered(world, getPos(), new ItemStack(Items.diamond,64));
				numDiamonds-=64;
			}
			Utils.spawnItemCentered(world, getPos(), new ItemStack(Items.diamond,numDiamonds));
			this.onPatternBroken();
		}

	}

	@Override
	public void update() {

	}

}
