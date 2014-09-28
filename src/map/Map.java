package map;

import java.util.ArrayList;

import util.RandUtil;
import util.Tuple;

/**
 * TODO RELEVANT:
 * - Create the doors algorithm between the rooms (should it be here or the room itself) -> Probably here
 * - Test
 * 
 * TODO ADDITIONAL: 
 * - Map of maps? So we can teleport to other maps and stuff.
 *
 */

public class Map {

	private Tuple<Integer, Integer> global_init;
	private Tuple<Integer, Integer> global_fin;
	private int real_x;
	private int real_y;
	byte[][] free_room;
	boolean is_there_free_space = true;
	private ArrayList<Room> rooms;
	private int size;

	/**
	 * Creates a random map, which is a collection of rooms
	 * @param global_x
	 * @param global_y
	 */
	
	public Map(Tuple<Integer, Integer> global_init, Tuple<Integer, Integer> global_fin){
		this.set_global_init(global_init);
		this.set_global_init(global_fin);
		real_x = this.global_fin().x - this.global_init().x;
		real_y = this.global_fin().y - this.global_init().y;
		size = real_x * real_y;
		free_room = new byte[real_x][real_y];
		initializeMatrixZero(free_room);
		rooms = new ArrayList<Room>();
		this.initialize_rooms_map();
	}
	
	/**
	 * Given the dimensions of the map, it returns the number of rooms
	 * that the map will have
	 * @return
	 */
	public int obtainNumberRooms(){
		double reduced_size = size/100;
		int min_num = (int) Math.floor(reduced_size);
		int max_num = (int) Math.ceil(reduced_size);
		return RandUtil.RandomNumber(min_num, max_num);
	}
	
	/**
	 * Given a byte matrix, it initializes it to 0
	 * @param matrix
	 * @return
	 */
	public void initializeMatrixZero(byte[][] matrix){
		int real_x = this.global_fin().x - this.global_init().x;
		int real_y = this.global_fin().y - this.global_init().y;
		for (int i = 0; i < real_x; i++){
			for (int j = 0; j < real_y; j++){
				matrix[i][j] = 0;
			}
		}
	}
	
	/**
	 * 
	 * @return Tuple of the position of the next room that we must extend
	 */
	public Tuple<Integer, Integer> obtainAvailableRoom(){
		int real_x = this.global_fin().x - this.global_init().x;
		int real_y = this.global_fin().y - this.global_init().y;
		ArrayList<Tuple<Integer, Integer>> possibleExtensionPoints = new ArrayList<>();
		if (this.free_room[0][0] == 0){
			possibleExtensionPoints.add(new Tuple<Integer, Integer>(0,0));
		}
		
		for (int i = 0; i < real_x; i++){
			for (int j = 0; j < real_y; j++){
				if (i == 0 && j != 0){
					if (free_room[0][j] == 0 && free_room[0][j-1] == 1){
						possibleExtensionPoints.add(new Tuple<Integer, Integer>(0, j-1));
					}
				}
				if (j == 0 && i != 0){
					if (free_room[i][0] == 0 && free_room[i-1][0] == 1){
						possibleExtensionPoints.add(new Tuple<Integer, Integer>(i-1, 0));
					}
				}
				if ((free_room[i-1][j-1] == 1 && free_room[i][j] == 0)){
					possibleExtensionPoints.add(new Tuple<Integer, Integer>(i-1, j-1));
				}
			}
		}
		
		int position_random_selected = RandUtil.RandomNumber(0, possibleExtensionPoints.size());
		
		return possibleExtensionPoints.get(position_random_selected);

	}
	
	
	/**
	 * Sets to 1 the space of the two rooms defined by the tuples in the free room array
	 * @param tuple1
	 * @param tuple2
	 */
	public void createRoomMatrix(Tuple<Integer, Integer> tuple1, Tuple<Integer, Integer> tuple2){
		int lowestX;
		int highestX;
		int lowestY;
		int highestY;
		
		if (tuple1.x < tuple2.x){
			lowestX = tuple1.x;
			highestX = tuple2.x;
		} else {
			lowestX = tuple2.x;
			highestX = tuple1.x;
		}
		
		if (tuple1.y < tuple2.y){
			lowestY = tuple1.y;
			highestY = tuple2.y;
		} else {
			lowestY = tuple2.y;
			highestY = tuple1.y;
		}
		
		for (int i = lowestX; i < highestX; i++){
			for (int j = lowestY; j < highestY; j ++){
				free_room[i][j] = 1;
			}
		}
	}
	

	/**
	 * Given a Tuple, it returns the length from that x and y to the next 1 of the free_room array.
	 * This is useful to know the space we have left in a certain space for the next room
	 * @param initial_tuple
	 * @return
	 */
	public Tuple<Integer, Integer> get_free_room_x_y(Tuple<Integer, Integer> initial_tuple){
		int initial_x = initial_tuple.x + 1;
		int initial_y = initial_tuple.y + 1;
		int final_x = 0;
		int final_y = 0;
		for (int i = initial_x; i < real_x; i++){
			if (free_room[i][initial_y] == 0){
				final_y++;
			} else break;
		}
		for (int j = initial_y; j < real_x; j++){
			if (free_room[initial_x][j] == 0){
				final_x++;
			} else break;
		}
		Tuple<Integer, Integer> free_x_and_y = new Tuple<Integer, Integer>(final_x, final_y);
		return free_x_and_y;
	}
	
	/**
	 * Given an initial point, it returns the other point where the rooms should be extended to
	 * @param originalPoint
	 * @return
	 */
	public Tuple<Integer, Integer> nextPoint(Tuple<Integer, Integer> originalPoint, int remainingRooms){

		Tuple<Integer, Integer> freeRoomSpace = get_free_room_x_y(originalPoint);
		int free_room_space_x = freeRoomSpace.x;
		int free_room_space_y = freeRoomSpace.y;
		if (remainingRooms == 1){
			int actual_x = originalPoint.x + free_room_space_x;
			int actual_y = originalPoint.y + free_room_space_y;
			Tuple<Integer, Integer> nextRoom = new Tuple<Integer, Integer>(actual_x, actual_y);
			return nextRoom;
		} else{
			double possible_real_x = free_room_space_x/remainingRooms;
			double possible_real_y = free_room_space_y/remainingRooms;
			int possible_real_x_low = (int) Math.floor(possible_real_x);
			int possible_real_x_high = (int) Math.ceil(possible_real_x);
			int possible_real_y_low = (int) Math.floor(possible_real_y);
			int possible_real_y_high = (int) Math.ceil(possible_real_y);
			int rand_number = RandUtil.RandomNumber(0, 1);
			if (rand_number == 0){
				int actual_x = originalPoint.x + possible_real_x_low;
				int actual_y = originalPoint.y + possible_real_y_low;
				Tuple<Integer, Integer> nextRoom = new Tuple<Integer, Integer>(actual_x, actual_y);
				return nextRoom;
			} else {
				int actual_x = originalPoint.x + possible_real_x_high;
				int actual_y = originalPoint.y + possible_real_y_high;
				Tuple<Integer, Integer> nextRoom = new Tuple<Integer, Integer>(actual_x, actual_y);
				return nextRoom;
			}
		}
	}

	/**
	 * Main function that creates a map given its size, using the rest of the
	 * class functions
	 */
	public void initialize_rooms_map(){
		int number_rooms = 0;
		Tuple<Integer, Integer> InitialPoint;
		Tuple<Integer, Integer> FinalPoint;
		
		while (number_rooms < size){
			InitialPoint = this.obtainAvailableRoom();
			FinalPoint = nextPoint(InitialPoint, size - number_rooms);
			Room r = new Room(InitialPoint, FinalPoint);
			this.rooms.add(r);
			number_rooms++;
		}
		complete_map();
	}
	
	/**
	 * Sets the is_there_free_space boolean value to true or false depending on 
	 * the matrix free_room. If it still has space in it (0 values), then it is
	 * true, if not; it is false.
	 * 
	 */
	public void check_free_space(){
		for (int i = 0; i < real_x; i++){
			for (int j = 0; j < real_y; j++){
				if (free_room[i][j] == 0){
					this.is_there_free_space = true;
					return;
				}
			}
		}
		this.is_there_free_space = false;
	}
	
	/**
	 * It is possible that after initializing a room there are some spaces without
	 * rooms, so this function will create rooms in those free spaces
	 */
	public void complete_map(){
		Tuple<Integer, Integer> InitialPoint;
		Tuple<Integer, Integer> FinalPoint;
		while (is_there_free_space){
			for (int i = 0; i < real_x; i++){
				for (int j = 0; j < real_y; j++){
					if (free_room[i][j] == 0){
						InitialPoint = new Tuple<Integer, Integer>(i, j);
						FinalPoint = nextPoint(InitialPoint, 1);
						Room r = new Room(InitialPoint, FinalPoint);
						this.rooms.add(r);
					}
				}
			}
			check_free_space();
		}
	}
	
	
	public Tuple<Integer, Integer> global_init() {
		return global_init;
	}

	public Tuple<Integer, Integer> global_fin() {
		return global_fin;
	}
	
	public void set_global_fin(Tuple<Integer, Integer> tuple) {
		this.global_fin = tuple;
	}
	
	public void set_global_init(Tuple<Integer, Integer> tuple) {
		this.global_init = tuple;
	}
	
	public int get_size() {
		return this.size;
	}
	
	public void set_size(int size) {
		this.size = size;
	}

	public static void main(String[] args) {

	}

}
