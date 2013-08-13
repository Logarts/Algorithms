import java.util.*;
class Solution {
	final int maxLength = 30;  //max length of board's side is 30
	public int current[]; 
	public int working[];
	public int best[];
	public int workEnergy;
	public int currEnergy;
	public int bestEnergy;
	//Initialization of solution (current and working, copies)
	public void init() {
		this.current = new int[maxLength];
		this.working = new int[maxLength];
		this.best = new int[maxLength];
		System.out.print("Initialized solution: "); //Make sure that everything fine
		for (int i=0; i<maxLength; i++) {
			this.current[i] = i+1;
			System.out.print(this.current[i] + ", ");
		}
		System.out.println(" ");
	}
	
	//Copy Current to working solution
	public void currToWork() {
		for (int i=0; i<maxLength; i++) {
			this.working[i] = this.current[i];
		}
	}
	
	//copy working to current solution
	public void workToCurr() {
		for (int i=0; i<maxLength; i++) {
			this.current[i] = this.working[i];
		}
	}
	
	public void currToBest() {
		for (int i=0; i<maxLength; i++) {
			this.best[i] = this.current[i];
		}
	}
	
	//searching another solution
	public void search() {
		Random rand = new Random();
		int temp, x, y;
		x = rand.nextInt(maxLength);
			do {
				y = rand.nextInt(maxLength);
			} while (x == y);
		temp = this.working[x];
		this.working[x] = this.working[y];
		this.working[y] = temp;
	}
	
	//Compute best energy
		public void betterEnergy() {
			//initialization of EVERITHING
			int i, j, x, y, tempx, tempy;
			int[][] board;
			board = new int[maxLength][maxLength];
			int conflicts;
			final int dx[] = {-1, 1, -1, 1};
			final int dy[] = {-1, 1, 1, -1};
			for (i = 0; i < maxLength; i++) {
				board[i][(this.best[i]-1)] = 1;
			}
			conflicts = 0; //to zero
			for (i = 0; i <maxLength; i++) {
				x = i;
				y = this.best[i] - 1;
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
			this.bestEnergy = conflicts;
		}
	
	//Compute working energy
	public void workingEnergy() {
		//initialization of EVERITHING
		int i, j, x, y, tempx, tempy;
		int[][] board;
		board = new int[maxLength][maxLength];
		int conflicts;
		final int dx[] = {-1, 1, -1, 1};
		final int dy[] = {-1, 1, 1, -1};
		for (i = 0; i < maxLength; i++) {
			board[i][(this.working[i]-1)] = 1;
		}
		conflicts = 0; //to zero
		for (i = 0; i <maxLength; i++) {
			x = i;
			y = this.working[i] - 1;
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
		this.workEnergy = conflicts;
	}
	
	//Compute current energy
		public void currentEnergy() {
			//initialization of EVERITHING
			int i, j, x, y, tempx, tempy;
			int[][] board;
			board = new int[maxLength][maxLength];
			int conflicts;
			final int dx[] = {-1, 1, -1, 1};
			final int dy[] = {-1, 1, 1, -1};
			for (i = 0; i < maxLength; i++) {
				board[i][(this.current[i]-1)] = 1;
			}
			conflicts = 0; //to zero
			for (i = 0; i <maxLength; i++) {
				x = i;
				y = this.current[i] - 1;
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
			this.currEnergy = conflicts;
		}
	
	//Matrix has you, Neo...
	public void matrix() {
		int i, j;
		int[][] board;
		board = new int[maxLength][maxLength];
		for (i = 0; i < maxLength; i++) {
			board[i][(this.best[i]-1)] = 1;
		}
		for (i = 0; i <maxLength; i++) {
			for ( j = 0; j <maxLength; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println(" ");
		}
	}
	//Just logs
	public void show() {
		System.out.print("Current solution: ");
		for (int i=0; i<maxLength; i++) {
			System.out.print(this.current[i] + ", ");
		}
		System.out.println(" ");
		System.out.print("Working solution: ");
		for (int i=0; i<maxLength; i++) {
			System.out.print(this.working[i] + ", ");
		}
		System.out.println(" ");
	}
}

//main class and realization of program
public class Annealing {
	public static void main(String[] args) {
		final double initialTemperature = 30.0;
		final double finalTemperature = 0.5;
		final double alpha = 0.98;
		final double steps = 100;
		int timer = 0;
		int step;
		int solution = 0;
		int useNew = 0;
		int accepted;
		double temperature = initialTemperature;
		
		Solution sol = new Solution(); //get an object
		sol.init(); //get working space
		
		System.out.println("My mind has been started...");  //Start of annealing algorithm
		System.out.println(" ");
		sol.currToWork();
		
		//Randomizing numbers in solution
		for (int i = 0; i < sol.maxLength; i++) {
			sol.search();
		}
		sol.show(); //just log
		sol.currentEnergy();
		sol.bestEnergy = 100;
		while (temperature > finalTemperature) {
			System.out.println("Temperature: " + temperature);
			accepted = 0;
			for (step = 0; step < steps; step++) {
				useNew = 0;
				sol.search();
				sol.workingEnergy();
				sol.currentEnergy();
				if (sol.workEnergy <= sol.currEnergy) {
					useNew = 1;
				}
				else {
					Random ran = new Random();
					double test = ran.nextDouble();
					double delta = sol.workEnergy - sol.currEnergy;
					double calc = Math.exp(-delta/temperature);
					if (calc > test) {
						accepted++;
						useNew = 1;
					}
				}
				if (useNew == 1) {
					useNew = 0;
					sol.workToCurr();
					if (sol.currEnergy < sol.bestEnergy) {
						sol.currToBest();
						sol.betterEnergy();
						solution = 1;
					}
				}
				else {
					sol.currToWork();
				}
			}
			System.out.println(timer++ + ", " + temperature + ", " + sol.bestEnergy + ", " + accepted);
			System.out.println("Best energy = " + sol.bestEnergy);
			temperature *= alpha;
			if (solution == 1) {
				sol.matrix();
				System.out.println("Best energy = " + sol.bestEnergy);
			}
		}
		
	}
}
