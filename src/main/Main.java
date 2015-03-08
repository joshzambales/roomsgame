package main;

import items.Item;
import items.consumables.LifePotion;
import items.wereables.OneHandSword;
import items.wereables.WereableArmor;
import items.wereables.WereableWeapon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

import characters.active.ActiveCharacter;
import characters.active.enemies.Goblin;
import util.RandUtil;
import util.Tuple;
import map.Map;
import map.Room;
import net.slashie.libjcsi.wswing.WSwingConsoleInterface;


public class Main {
	public static String language = new String("es");
	public static String country = new String("ES");
	public static Locale currentLocale = new Locale(language, country);
	public static ResourceBundle messagesWereables;
	public static boolean debug = false;
	public static boolean testMode = false;
	public static char[] usedSymbols = {'.', 'P', 'G', 'A'};
	static Tuple<Integer, Integer> initial_point = new Tuple<Integer, Integer>(0, 0);
	static Tuple<Integer, Integer> final_point = new Tuple<Integer, Integer>(20, 20);
	static Integer[] movementInput = new Integer[] {0, 1, 2, 3};
	static Integer[] inventoryInput = new Integer[] {131, 132, 133, 134, 135, 136};
	static Integer[] pickItemInput = new Integer[] {68};
	static Integer[] attackInput = new Integer[] {87};
	
	
	public static Room getRandomRoom(Map map){
		return map.getRooms().get(RandUtil.RandomNumber(0, map.getRooms().size()));
	}
	
	public static boolean isMovementInput(int key){
		return Arrays.asList(movementInput).contains(key);
	}
	
	public static boolean isInventoryInput(int key){
		return Arrays.asList(inventoryInput).contains(key);
	}
	
	public static boolean isPickItemInput(int key){
		return Arrays.asList(pickItemInput).contains(key);
	}
	
	public static boolean isAttackInput(int key){
		return Arrays.asList(attackInput).contains(key);
	}

	public static void main(String[] args) throws IOException {

		messagesWereables = ResourceBundle.getBundle("translations.files.MessagesWereable", currentLocale);
		
		if (!testMode){
			Map map = new Map(initial_point, final_point);
			Tuple<Integer, Integer> pos = new Tuple<Integer, Integer>(1,1);
			Room roomCharacter = getRandomRoom(map);
			Room roomEnemy = getRandomRoom(map);
			char previousPositionChar = '.';
			char previousPositionChar2 = '.';
			boolean firstTime = true;
			boolean hasChanged = false;
			ActiveCharacter user = new ActiveCharacter("", "", "", map, roomCharacter, roomCharacter.getRandomInsidePosition(), 
					40, 0, 100, 100, 100, 100, new ArrayList<WereableWeapon>(),
					new ArrayList<WereableArmor>(), 100, 100, 0,
					new ArrayList<Item>(), 0, 0, 100, 100, 100, "@", 4, 0);
			
			WSwingConsoleInterface j = new WSwingConsoleInterface("asdasd");
			j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
			LifePotion lifePotion30 = new LifePotion(0, 10, "", null, null, null, null, 30);
			lifePotion30.setCharacter(user);
			LifePotion lifePotion40 = new LifePotion(0, 10, "", null, null, null, null, 30);
			lifePotion40.setCharacter(user);
			LifePotion lifePotion50 = new LifePotion(0, 10, "", null, null, null, null, 30);
			lifePotion50.setCharacter(user);
			LifePotion lifePotion60 = new LifePotion(0, 10, "", null, null, null, pos, 30);
			map.putItemRoom(lifePotion60);
			WereableWeapon oneHandSword = new OneHandSword("", 0, 0, 100, user, null, null,
					null, 0, 0, true);
			WereableWeapon oneHandSword2 = new OneHandSword("", 0, 0, 100, user, null, null,
					null, 0, 0, true);
			WereableWeapon oneHandSword3 = new OneHandSword("", 0, 0, 100, null, null, null,
					null, 0, 0, true);
			WereableWeapon oneHandSword4 = new OneHandSword("", 0, 0, 100, null, null, null,
					null, 0, 0, true);
			Goblin goblin = new Goblin(map, roomEnemy, roomEnemy.getRandomPosition(), 0, new ArrayList<Item>());
			goblin.putItemInventory(oneHandSword2);
			goblin.putItemInventory(oneHandSword3);
			goblin.putItemInventory(oneHandSword3);
			goblin.equipWeapon(oneHandSword3);
			goblin.equipWeapon(oneHandSword4);
			roomEnemy.getMonsters().add(goblin);
			
			ArrayList<Item> inventory = new ArrayList<Item>();
			inventory.add(lifePotion30);
			inventory.add(lifePotion40);
			inventory.add(lifePotion50);
			inventory.add(oneHandSword);
			user.setInventory(inventory);
			map.printBorders(j, user);
			map.printInside(j, user);
			map.printItems(j, user);
			map.printMonsters(j, user);
			user.printInventory(user.getInventory(), j, 22, 0);
			j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
			user.printInventory(user.getInventory(), j, 22, 0);
			j.refresh();
			user.setLife(80);
			for (;;) {
				System.out.println("Vida user: " + user.getLife());
				int i = j.inkey().code;
				j.cls();
				map.printBorders(j, user);
				map.printInside(j, user);
				map.printItems(j, user);
				map.printMonsters(j, user);
				user.printInventory(user.getInventory(), j, 22, 0);
				j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	            System.out.println(i);
	            Tuple<Integer, Integer> previousPosition = user.getPosition();
	            Tuple<Integer, Integer> newPosition = RandUtil.inputMoveInterpretation(i, user);
	            if (isMovementInput(i)){
	            	System.out.println("HOLA MOVE");
		            if (user.move(newPosition)){
		            	System.out.println("HOLA MOVE2");
		            	user.setVisiblePositions();
		            	map.printBorders(j, user);
		    			map.printInside(j, user);
		    			map.printItems(j, user);
		    			map.printMonsters(j, user);
		            	previousPositionChar = previousPositionChar2;
		            	previousPositionChar2 = j.peekChar(newPosition.y, newPosition.x);
		            	System.out.println("Has Changed: " + hasChanged);
		            	if (RandUtil.containsString(usedSymbols, j.peekChar(newPosition.y, newPosition.x))){
		            		j.print(newPosition.y, newPosition.x, user.getSymbolRepresentation(), 12);
			            	j.print(previousPosition.y, previousPosition.x, previousPositionChar, 12);
			            	System.out.println("HOLA");
			            	if (hasChanged){
			            		Tuple<Integer, Integer> newPositionRoom = new Tuple<Integer, Integer>(newPosition.x, newPosition.y - 1);
			            		j.print(newPosition.y, newPosition.x, user.getSymbolRepresentation(), 12);
			            		System.out.println(map.getSymbolPosition(newPositionRoom));
				            	j.print(previousPosition.y, previousPosition.x, map.getSymbolPosition(newPositionRoom), 12);
				            	hasChanged = false;
			            	}
			            	
		            	} else{
		            		if (firstTime) {
		            			j.print(newPosition.y, newPosition.x, user.getSymbolRepresentation(), 12);
				            	j.print(previousPosition.y, previousPosition.x, previousPositionChar, 12);
		            			firstTime = false;
		            		} else {
		            			j.print(newPosition.y, newPosition.x, user.getSymbolRepresentation(), 12);
				            	j.print(previousPosition.y, previousPosition.x, previousPositionChar2, 12);
		            			firstTime = true;
		            		} 
		            	}
		            }
	            }
	            else if (isInventoryInput(i)){
	            	System.out.println(user.getWeaponsEquipped().size());
	            	user.useItem(user.getInventory().get(i%131));
	            	j.cls();
					map.printBorders(j, user);
					map.printInside(j, user);
					map.printItems(j, user);
					map.printMonsters(j, user);
					user.printInventory(user.getInventory(), j, 22, 0);
					System.out.println(user.getWeaponsEquipped().size());
					j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
	            }
	            else if (isPickItemInput(i)){
	            	if (user.pickItem(user.getPosition(), user.getRoom())){
	            		j.cls();
						map.printBorders(j, user);
						map.printInside(j, user);
						map.printItems(j, user);
						map.printMonsters(j, user);
						user.printInventory(user.getInventory(), j, 22, 0);
						j.print(user.getPosition().y, user.getPosition().x, user.getSymbolRepresentation(), 12);
						hasChanged = true;
	            	}
	            }
	            else if (isAttackInput(i)){
	            	System.out.println("Has Changed attach" + hasChanged);
	            	if (map.getMonstersPosition(user).size() > 0){
	            		ActiveCharacter monster = map.getMonstersPosition(user).get(0);
	            		System.out.println("Vida monster: " + map.getMonstersPosition(user).get(0).getLife());
	            		user.attack(monster);
	            		System.out.println(monster.getWeaponsEquipped().size());
	            		if (monster.getLife() <= 0){
	            			System.out.println("Item position: ");
	            			System.out.println(monster.getInventory().get(0).getPosition().x);
	            			System.out.println(monster.getInventory().get(0).getPosition().y);
	            			hasChanged = true;
	            		}
	            		System.out.println("Vida monster: " + map.getMonstersPosition(user).get(0).getLife());
	            	}
	            }
	            user.getRoom().monsterTurn(user);
	            System.out.println("Vida user: " + user.getLife());
	            
				j.refresh();
			}
		}
	}
}
