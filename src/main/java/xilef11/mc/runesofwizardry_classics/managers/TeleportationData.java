package xilef11.mc.runesofwizardry_classics.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import xilef11.mc.runesofwizardry_classics.ModLogger;
import xilef11.mc.runesofwizardry_classics.Refs;
/**
 * Manages and save the data for teleportation network
 * @see http://mcforge.readthedocs.org/en/latest/datastorage/worldsaveddata/
 * @author Xilef11
 *
 */
public class TeleportationData extends WorldSavedData {
	private static final String DATA_NAME = Refs.MODID + "_TeleportationData";
	public TeleportationData(){
		this(DATA_NAME);
	}
	public TeleportationData(String name) {
		super(name);
	}
	/**
	 * Get the saved teleportation data for a world (or a new instance if there is none)
	 * @param world the world for which to get the teleportation network data
	 * @return the (possibly empty) teleportation network data for a world
	 */
	public static TeleportationData get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		TeleportationData instance = (TeleportationData) storage.loadData(TeleportationData.class, DATA_NAME);
		if (instance == null) {
			instance = new TeleportationData();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}
	//the key is the "id" of a blockstate in the form blockid@meta
	private Map<String,Set<BlockPos>> networks = new HashMap<String, Set<BlockPos>>();
	/**
	 * Adds a teleport location to a network
	 * @param state the BlockState that identifies the network
	 * @param pos the position to add to the network
	 */
	public void registerLocation(IBlockState state, BlockPos pos){
		//get the key for this blockstate
		String key = getStateID(state);
		//add the position for this state
		addLocation(key, pos);
		this.markDirty();
		/*markDirty here instead of in addLocation 
		 * because addLocation is called when reading from NBT (which is not a change in the data)
		 * maybe add it to readFromNBT if saving doesn't work properly on servers
		 */
	}
	/**
	 * Removes a teleport location from a network
	 * @param state the BlockState that identifies the network
	 * @param pos the position to remove from the network
	 */
	public void removeLocation(IBlockState state, BlockPos pos){
		String key = getStateID(state);
		Set<BlockPos> locations = networks.get(key);
		if(locations!=null){
			locations.remove(pos);
		}else{
			ModLogger.logWarn("Trying to remove a teleport destination with inexistant key: "+key+" for state: "+state+" at pos: ");
		}
		this.markDirty();
	}
	/**
	 * Returns a set of possible destinations for a teleportation network, or null if none exists.
	 * @param state the IBlockState that identifies the teleportation network
	 * @return a set of the possible destinations (/!\ Not a copy), or null if there are none.
	 */
	public Set<BlockPos> getDestinations(IBlockState state){
		return networks.get(getStateID(state));
	}
	
	private void addLocation(String key, BlockPos pos){
		//get the list of positions attached to this blockstate id
		Set<BlockPos> locations = networks.get(key);
		if(locations==null){
			locations = new HashSet<BlockPos>();
			networks.put(key, locations);
		}
		//add the position to the list
		locations.add(pos);
	}
	
	private String getStateID(IBlockState state){
		//get the key for this blockstate
		Block block = state.getBlock();
		ResourceLocation blockname = Block.blockRegistry.getNameForObject(block);
		int meta = block.getMetaFromState(state);
		String key = blockname.toString()+"@"+meta;
		return key;
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		//WizardryLogger.logInfo("Reading teleportation data from NBT");
		networks.clear();//clear the network map to avoid duplicates on packet reception
		NBTTagList keys = (NBTTagList) nbt.getTag(DATA_NAME);
		for(int i=0;i<keys.tagCount();i++){
			NBTTagCompound key = keys.getCompoundTagAt(0);
			//grab the key
			String keyStr = key.getString("keystring");
			//get the list of positions
			NBTTagList positions = (NBTTagList) key.getTag("positions");
			for(int j=0;j<positions.tagCount();j++){
				NBTTagCompound coords = positions.getCompoundTagAt(j);
				int x = coords.getInteger("X");
				int y = coords.getInteger("Y");
				int z = coords.getInteger("Z");
				BlockPos pos = new BlockPos(x, y, z);
				addLocation(keyStr, pos);
			}
		}
	}
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		//WizardryLogger.logInfo("Writing teleportation data to NBT");
		NBTTagList keys = new NBTTagList();
		for(String state:networks.keySet()){
			NBTTagCompound key = new NBTTagCompound();
			NBTTagList positions = new NBTTagList();
			for(BlockPos pos:networks.get(state)){
				NBTTagCompound coords = new NBTTagCompound();
				coords.setInteger("X", pos.getX());
				coords.setInteger("Y", pos.getY());
				coords.setInteger("Z", pos.getZ());
				positions.appendTag(coords);
			}
			//save blockstate ID
			key.setString("keystring", state);			
			//save positions for current key
			key.setTag("positions", positions);
			//add current key to list
			keys.appendTag(key);
		}
		nbt.setTag(DATA_NAME, keys);
	}

}
