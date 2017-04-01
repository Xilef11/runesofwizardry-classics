package xilef11.mc.runesofwizardry_classics.runes;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;
import xilef11.mc.runesofwizardry_classics.items.DustVariable;
import xilef11.mc.runesofwizardry_classics.utils.Utils.Coords;
/**
 * Handles Runes with variable dust that must match
 * @author Xilef11
 *
 */
//FUTURE This would be better as an interface (for RuneEntity) with default values, because we wouldn't need to expose patternMatches in IRune. however, we don't want to be Java 8 only.
public abstract class VariableRune extends ClassicRune {
	private Set<Coords> variables=null;
	/**
	 * Returns all variable dust positions in this rune.
	 * Not a super efficient default implementation, subclasses should override if they know the variable positions at compile time
	 */
	public Set<Coords> getVariableDusts(){
		if(variables==null){
			variables=new HashSet<Coords>();
			ItemStack[][] pattern = getPattern();
			for(int r = 0;r<pattern.length;r++){
				for(int c=0;c<pattern[r].length;c++){
					ItemStack s = pattern[r][c];
					if(s.getItem()==DustVariable.instance){
						variables.add(new Coords(r,c));
					}
				}
			}
		}
		return variables;
	}
	/* (non-Javadoc)
	 * @see com.zpig333.runesofwizardry.api.IRune#patternMatches(net.minecraft.item.ItemStack[][], net.minecraft.item.ItemStack[][])
	 */
	@Override
	public boolean patternMatchesExtraCondition(ItemStack[][] foundPattern) {
		return variablesOK(foundPattern);
	}
	/** checks if all variable dusts are the same in the found pattern
	 * Could be overridden to also check for specific levels of variable dusts
	 * @param foundPattern the pattern found
	 * @return true if the variable dust positions are valid
	 */
	protected boolean variablesOK(ItemStack[][] foundPattern) {
		ItemStack firstVar=ItemStack.EMPTY;
		for(Coords c:getVariableDusts()){
			if(firstVar.isEmpty())firstVar=foundPattern[c.row][c.col];
			if(foundPattern[c.row][c.col].isEmpty() || !ItemStack.areItemStacksEqual(firstVar, foundPattern[c.row][c.col]))return false;
		}
		return true;
	}
}
