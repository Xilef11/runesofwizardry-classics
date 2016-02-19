/**
 * 
 */
package xilef11.mc.runesofwizardry_classics.items;

/**
 * @author Xilef11
 *
 */
public enum EnumDustTypes {
	PLANT (0,0),
	GUNPOWDER(0,0),
	LAPIS(0,0),
	BLAZE(0,0);
	/*
	 * In order of least valuable to most valuable:
	   2xTall Grass or Saplings or Leaves or Seeds + 1xCoal = 8xPlant Runic Dust
       2xPlant Runic Dust + 1xGunpowder = 12xGunpowder Runic Dust
       3xLapis + 1xCoal = 8xLapis Runic Dust
       3xLapis Runic Dust + 1xBlaze Powder = 12xBlaze Runic Dust
	 */
	public final int primaryColor, secondaryColor;
	//constructor for our dust types
	EnumDustTypes(int primaryColor, int secondaryColor){
		this.primaryColor=primaryColor;
		this.secondaryColor=secondaryColor;
	}
	/** returns the metadata value associated with a dust type **/
	public int meta(){
		return this.ordinal();
	}
	/** returns the dust type associated with the given meta **/
	public static EnumDustTypes getByMeta(int meta){
		return EnumDustTypes.values()[meta];
	}
}
