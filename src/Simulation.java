import java.util.*;
class Solt {
	public static final int maxLength = 30;
	public int solution[];  
	public int energy; 
	
	//Method of solution initialization
	public void init() {
		this.solution = new int [maxLength];
		for (int i=0; i < maxLength; i++) {
			this.solution[i] = i + 1;
		}
	}
	
	//Method of copying src to dst
	public static void copy(int src[], int dst[]) {
		for (int i=0; i < maxLength; i++) {
			dst[i] = src[i];
		}
	}
	
	//Method of searching random solution
	public void search() {
		Random rand = new Random();
		int temp, x, y;
		x = rand.nextInt(maxLength);
			do {
				y = rand.nextInt(maxLength);
			} while (x == y);
		temp = this.solution[x];
		this.solution[x] = this.solution[y];
		this.solution[y] = temp;
	}
	
	//Method of computing solution's energy
	public void computeEnergy() {
		//initialization of EVERITHING
		int i, j, x, y, tempx, tempy;
		int[][] board;
		board = new int[maxLength][maxLength];
		int conflicts;
		final int dx[] = {-1, 1, -1, 1};
		final int dy[] = {-1, 1, 1, -1};
		for (i = 0; i < maxLength; i++) {
			board[i][(this.solution[i]-1)] = 1;
		}
		conflicts = 0; //to zero
		for (i = 0; i < maxLength; i++) {
			x = i;
			y = this.solution[i] - 1;
			for (j = 0; j < 4; j++) {
				tempx = x;
				tempy = y;
				while(true) {
					tempx += dx[j];
					tempy += dy[j];
					if ((tempx <0) || (tempx >= maxLength) || (tempy < 0) || (tempy >= maxLength)) break;
					if (board[tempx][tempy] == 1) conflicts++;
				}
			}
		}
		this.energy = conflicts;
	}
	
	//Method that show us Matrix...
	public void matrix() {
		int i, j;
		int[][] board;
		board = new int[maxLength][maxLength];
		for (i = 0; i < maxLength; i++) {
			board[i][(this.solution[i]-1)] = 1;
		}
		for (i = 0; i <maxLength; i++) {
			for ( j = 0; j <maxLength; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println(" ");
		}
	}
}
public class Simulation {
	public static void main(String[] args) {
		final double initialTemperature = 50.0;
		final double finalTemperature = 0.5;
		final double alpha = 0.99;
		final double steps = 150;
		int timer = 0;
		int step;
		int solution = 0;
		int useNew = 0;
		int accepted;
		double temperature = initialTemperature;
		Solt current = new Solt();
		current.init();
		Solt working = new Solt();
		working.init();
		Solt best = new Solt();
		best.init();
		
		
		for (int i = 0; i < Solt.maxLength; i++) {
			current.search();
		}
		current.computeEnergy();
		Solt.copy(current.solution, working.solution);
		best.energy = 1;
		while (temperature > finalTemperature) {
			//System.out.println("Temperature: " + temperature);
			accepted = 0;
			for (step = 0; step < steps; step++) {
				useNew = 0;
				working.search();
				working.computeEnergy();
				if (working.energy <= current.energy) {
					useNew = 1;
				} else {
					Random ran = new Random();
					double test = ran.nextDouble();
					double delta = working.energy - current.energy;
					double calc = Math.exp(-delta/temperature);
					if (calc > test) {
						accepted++;
						useNew = 1;
					}
				}
				if (useNew == 1) {
					useNew = 0;
					Solt.copy(working.solution, current.solution);
					current.computeEnergy();
					if (current.energy < best.energy) {
						Solt.copy(current.solution, best.solution);
						best.computeEnergy();
						solution = 1;
					}
				} else {
					Solt.copy(current.solution, working.solution);
				}
			}
			//timer++; //get tfoh
			System.out.println("Step: " + timer++ + ", temp: " + temperature + ", energy: " + current.energy + ", accepted: " + accepted);
			temperature *= alpha;
			if (solution == 1) {
				System.out.println("Success!");
				best.matrix();
				break;
				//return 1;
			}
		}
		//return 0;
	}
}
