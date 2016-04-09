package xilef11.mc.runesofwizardry_classics.managers;

import xilef11.mc.runesofwizardry_classics.Refs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
/** Saves the number of active runes of Locked Time in a world, to stop falling blocks **/
public class LockedTimeData extends WorldSavedData{
	private static final String DATA_NAME = Refs.MODID + "_LockedTimeData";
	public LockedTimeData(){
		this(DATA_NAME);
	}
	public LockedTimeData(String name) {
		super(name);
	}
	/**
	 * Get the saved Locked Time data for a world (or a new instance if there is none)
	 * @param world the world for which to get the teleportation network data
	 * @return the (possibly empty) Locked time runes data for a world
	 */
	public static LockedTimeData get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		LockedTimeData instance = (LockedTimeData) storage.loadData(LockedTimeData.class, DATA_NAME);
		if (instance == null) {
			instance = new LockedTimeData();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}
	private int runeNumber=0;
	
	public void addRune(){
		runeNumber++;
	}
	public void removeRune(){
		runeNumber--;
		if(runeNumber<0)runeNumber=0;
	}
	public int getNumRunes(){
		return runeNumber;
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		runeNumber = nbt.getInteger("LockedTime:runeNumber");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("LockedTime:runeNumber", runeNumber);		
	}

}
