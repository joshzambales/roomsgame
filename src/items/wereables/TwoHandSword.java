package items.wereables;

import items.ItemEnumerate.WeaponType;

import java.util.ArrayList;

import map.Map;
import map.Room;
import translations.Translations;
import util.Tuple;
import characters.Character;

public class TwoHandSword extends WereableWeapon {
	
	ArrayList<String> attributes = new ArrayList<String>();
	
	public TwoHandSword (String description, int weight,
			int space, int durability, Character character, Map map, Room room,
			Tuple<Integer, Integer> position, int erosion,
			int level, boolean isMagic) {
		//TODO: Change the 10 (which is the attack of the weapon) to the algorithm based on level
		super("sword", null, description, "m", 
				weight, space, durability, character, 
				new ArrayList<WeaponType>(),
				map, room, position, 10, erosion, true, 0, level, isMagic);
		this.setNameAttributes(this.getTwoHandSwordAttributes());
		//TODO: Change this to be a function
		ArrayList<WeaponType> weaponType = new ArrayList<WeaponType>();
		weaponType.add(WeaponType.LEFTHAND);
		weaponType.add(WeaponType.RIGHTHAND);
		this.setWeaponType(weaponType);
		if (isMagic){
			attributes = this.getNameAttributes();
			attributes.add("magic");
			this.setNameAttributes(attributes);
		}
		this.setName(Translations.getNameItem("sword", this.getNameAttributes()));
		this.setAttributes(this.getLevel(), false);
		
	}
	
	public ArrayList<String> getTwoHandSwordAttributes(){
		attributes.add("two");
		attributes.add("hand");
		return attributes;
	}

}
