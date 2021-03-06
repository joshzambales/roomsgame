package characters.active.enemies;

import items.Item;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;

import java.util.ArrayList;

import characters.active.ActiveCharacter;
import map.Map;
import map.Room;
import util.Tuple;

public class Goblin extends ActiveCharacter {

	public Goblin(Map map, Room room, Tuple<Integer, Integer> position, int inventorySpace,
			ArrayList<Item> inventory) {
		super("goblin", "", "m", map, room, position, 2, 1, 30,
				50, 100, 100, new ArrayList<WereableWeapon>(), new ArrayList<WereableArmor>(), 0,
				0, 0, inventory, 0,
				0, 100, 0, 0, "G", 3, 2);
	}
	
	

}
