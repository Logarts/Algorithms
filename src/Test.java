interface Capacity {
	public double getCapacity();
}
class Base implements Capacity {
	public double width;
	public double height;
	public double length;
	
	public double getCapacity() {
		return width*height*length;
	}
}

public class Test {
	public static void main(String[] args) {
		Base box = new Base();
		box.width = 1.0;
		box.height = 2.0;
		box.length = 3.0;
		Base room = new Base();
		room.width = 10.0;
		room.height = 20.0;
		room.length = 30.0;
		System.out.println("Size of box: " + box.getCapacity());
		System.out.println("Size of room: " + room.getCapacity());
		System.out.println("True size of room: " + (room.getCapacity() - box.getCapacity()));
	}

}
