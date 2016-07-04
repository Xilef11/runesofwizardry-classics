/**
 *
 */
package xilef11.mc.runesofwizardry_classics.runes.entity;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import xilef11.mc.runesofwizardry_classics.Refs;
import xilef11.mc.runesofwizardry_classics.utils.Utils;

import com.zpig333.runesofwizardry.api.IRune;
import com.zpig333.runesofwizardry.api.RuneEntity;
import com.zpig333.runesofwizardry.tileentity.TileEntityDustActive;
/**
 * @author Xilef11
 *
 */
public class RuneEntityRebirth extends RuneEntity {
	private static final String NO_SPAWN_EGG = Refs.Lang.RUNE+".rebirth.noegg";
	/**
	 * @param actualPattern
	 * @param facing
	 * @param dusts
	 * @param entity
	 */
	public RuneEntityRebirth(ItemStack[][] actualPattern, EnumFacing facing,
			Set<BlockPos> dusts, TileEntityDustActive entity,IRune creator) {
		super(actualPattern, facing, dusts, entity,creator);
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#onRuneActivatedbyPlayer(net.minecraft.entity.player.EntityPlayer, net.minecraft.item.ItemStack[], boolean)
	 */
	@Override
	public void onRuneActivatedbyPlayer(EntityPlayer player,ItemStack[] sacrifice, boolean negated) {
		World world = player.worldObj;
		if(!world.isRemote){
			//take xp if not negated
			if(!negated){
				if(player.experienceLevel>=10){
					player.removeExperienceLevel(10);
				}else{
					//kill the rune
					this.onPatternBrokenByPlayer(player);
					return;
				}
			}
			double x,y,z,offset=0.25;
			x=y=z=0;
			int dir = face.getAxisDirection()==AxisDirection.NEGATIVE? -1:1;
			switch(face.getAxis()){
			case X:x=offset*dir;
				break;
			case Y:y=offset*dir;
				break;
			case Z:z=offset*dir;
				break;
			}
			entity.setupStar(0xFFFFFF, 0xFFFFFF,1,1,new Vec3d(x, y, z));
			entity.setDrawStar(true);
		}
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#update()
	 */
	@Override
	public void update() {
		// not much here
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.RuneEntity#handleEntityCollision(net.minecraft.world.World, net.minecraft.util.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.Entity)
	 */
	@Override
	public boolean handleEntityCollision(World worldIn, BlockPos pos,IBlockState state, Entity entityIn) {
		if(!worldIn.isRemote){
			if((entityIn instanceof EntityLivingBase) && !(entityIn instanceof EntityPlayer)&&!(entityIn instanceof EntityArmorStand)){
				String entID = EntityList.CLASS_TO_NAME.get(entityIn.getClass());
				//check if a spawn egg exists TODO also config blacklist?
				if(EntityList.ENTITY_EGGS.get(entID)!=null){
					ItemStack egg = new ItemStack(Items.SPAWN_EGG);
					NBTTagCompound entityTag = new NBTTagCompound();
					entityTag.setString("id", entID);
					egg.setTagInfo("EntityTag", entityTag);
					Utils.spawnItemCentered(worldIn, pos, egg);
					this.onPatternBroken();//kill the rune
					entityIn.setDead();//kill the entity
				}else{
					worldIn.getClosestPlayer(pos.getX(),pos.getY(),pos.getZ(), 16, false).addChatMessage(new TextComponentTranslation(NO_SPAWN_EGG));
				}
			}
		}
		return true;
	}
}
