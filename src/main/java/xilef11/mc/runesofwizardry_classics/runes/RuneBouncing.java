
package xilef11.mc.runesofwizardry_classics.runes;

import java.io.IOException;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.runes.entity.RuneEntityBouncing;

import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.core.WizardryRegistry;
import com.zpig333.runesofwizardry.core.rune.PatternUtils;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustPlaced;

public class RuneBouncing extends ClassicRune {
	public RuneBouncing(){
		MinecraftForge.EVENT_BUS.register(this);
	}
	@Override
	public String getID() {
		return "runeBouncing";
	}
	@Override
	protected ItemStack[][] setupPattern() throws IOException {
		return PatternUtils.importFromJson(Refs.PATTERN_PATH+"runeBouncing.json");
	}

	@Override
	protected Vec3i setupEntityPos() {
		return new Vec3i(0,0,0);
	}

	@Override
	protected ItemStack[][] setupSacrifice() {
		return new ItemStack[][]{
				{new ItemStack(Items.slime_ball,4)}
				};
	}

	@Override
	public String getName() {
		return Refs.Lang.RUNE+".bouncing";
	}

	@Override
	public RuneEntity createRune(ItemStack[][] actualPattern, EnumFacing front,
			Set<BlockPos> dusts, TileEntityDustActive entity) {
		return new RuneEntityBouncing(actualPattern, front, dusts, entity,this);
	}
	//event handling is done here because this is a singleton, not the rune entity
	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event){
		EntityLivingBase ent = event.entityLiving;
		World world = ent.worldObj;
		if(!world.isRemote){
			//basically, check if we are standing on placed dust, then check if its an active rune of bouncing
			BlockPos feetPos = ent.getPosition();
			IBlockState state = world.getBlockState(feetPos);
			if(state.getBlock()==WizardryRegistry.dust_placed){
				TileEntity te = world.getTileEntity(feetPos);
				if(te instanceof TileEntityDustPlaced){
					TileEntityDustPlaced ted = (TileEntityDustPlaced)te;
					RuneEntity rune = ted.getRune();
					if(rune instanceof RuneEntityBouncing){
						ent.addVelocity(0, 1D, 0);
						ent.velocityChanged=true;
					}
				}
			}
		}
	}
}

    