import java.lang.String;
class ArtData {
	public static final int maxItems = 11;
	public static final int maxCustomers = 10;
	public static final int totalPrototypeVectors = 5;
	public static final double beta = 1.0;
	public static final double vigilance = 0.9;
	public final int[][] database = {
			{0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0},
			{0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1},
			{0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1},
			{0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1},
			{1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0},
			{0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
			{1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
			{0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
			{0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0}
	};
	public int[][] prototypeVector;
	public int[][] sumVector;
	public int[] members;
	public int[] membership;
	public int numPrototypeVectors = 0;
	
//	string[] itemName = {
//			"Hammer", "Paper", "Snickers", "Screwdriver", "Pen", "Kit-Kat", "Wreanch", "Pencil", "Heath-bar", "Tape-Measure", "Binder"};
//	};
	
	public void init() {
		this.prototypeVector = new int[totalPrototypeVectors][maxItems];
		this.sumVector = new int[totalPrototypeVectors][maxItems];
		this.members = new int[totalPrototypeVectors];
		this.membership = new int[maxCustomers];
		for (int i = 0; i < totalPrototypeVectors; i++) {
			for (int j = 0; j < maxItems; j++){
				this.prototypeVector[i][j] = 0;
				this.sumVector[i][j] = 0;
			}
			this.members[i] = 0;
		}
		for (int j = 0; j < maxCustomers; j++) {
			membership[j] = -1;
		}
	}
	int vectorMagnitude(int[] vector) {
		int j, total =0;
		for (j = 0; j < maxItems; j++) {
			if (vector[j] == 1) {
				total++;
			}
		}
		return total;
	}
	void vectorBitwiseAnd(int[] result, int[] v, int[] w) {
		int i;
		for (i = 0; i < maxItems; i++) {
			result[i] = (v[i] * w[i]);
		}
	}
	int createNewPrototypeVector(int[] example) {
		int i, cluster;
		for (cluster = 0; cluster < totalPrototypeVectors; cluster++) {
			if (this.members[cluster] == 0) break;
		}
		if (cluster == totalPrototypeVectors) {
			assert(false);
		}
		this.numPrototypeVectors++;
		for (i = 0; i < maxItems; i++) {
			this.prototypeVector[cluster][i] = example[i];
		}
		members[cluster] = 1;
		return cluster;
	}
	void updateProtorypeVectors(int cluster) {
		int item, customer, first =1;
		assert (cluster >=0);
		for ( item =0; item < maxItems; item++) {
			this.prototypeVector[cluster][item] = 0;
			this.sumVector[cluster][item] = 0;
		}
		for(customer = 0; customer < maxCustomers; customer++) {
			if (this.membership[customer] == cluster) {
				if (first == 1) {
					for (item = 0; item < maxItems; item++) {
						this.prototypeVector[cluster][item] = this.database[customer][item];
						this.sumVector[cluster][item] = this.database[customer][item];
					}
					first =0;
				} else {
					for (item = 0; item < maxItems; item++) {
						this.prototypeVector[cluster][item] = this.prototypeVector[cluster][item] * this.database[customer][item];
						this.sumVector[cluster][item] += this.database[customer][item];
					}
				}
			}
		}
		return;
	}
	int ArtAlgorythm() {
		int andresult[] = new int[maxItems];
		int pvec, magPE, magP, magE;
		double result, test;
		int index, done = 50;
		int count = 50;
		while (done != 0) {
			done = 1;
			for (index = 0; index < maxCustomers; index++) {
				for (pvec = 0; pvec < totalPrototypeVectors; pvec++) {
					if (members[pvec] >= 1) {
						this.vectorBitwiseAnd(andresult, this.database[index], this.prototypeVector[pvec]);
						magPE = this.vectorMagnitude(andresult);
						magP = this.vectorMagnitude(this.prototypeVector[pvec]);
						magE = this.vectorMagnitude(this.database[index]);
						result = (double)magPE / (beta + (double)magP);
						test = (double)magE / (beta + (double)maxItems);
						if (result > test) {
							if (((double)magPE / (double)magE) < vigilance) {
								int old;
								if (this.membership[index] != pvec) {
									old = this.membership[index];
									this.membership[index] = pvec;
									if (old >= 0) {
										this.members[old]--;
										if (members[old] == 0) {
											this.numPrototypeVectors--;
										}
									}
									this.members[pvec]++;
									if ((old >= 0) && (old < totalPrototypeVectors)) {
										this.updateProtorypeVectors(old);
									}
									this.updateProtorypeVectors(pvec);
									done = 0;
									break;
								} else {
									
								}
							}
						}
					}
				}
				if (this.membership[index] == -1) {
					this.membership[index] = this.createNewPrototypeVector(this.database[index]);
					done = 0;
				}
			}
			if (0 != count--) break;
		}
		
		return 0;
	}
	void makeRecomendation (int customer) {
		int bestItem = -1;
		int val = 0;
		int item;
		for (item = 0; item < maxItems; item++) {
			if ((this.database[customer][item] == 0) && (this.sumVector[this.membership[customer]][item] > val)) {
				bestItem = item;
				val = this.sumVector[this.membership[customer]][item];
			}
		}
		System.out.println("For customer " + customer);
		if (bestItem >= 0) {
			System.out.println("The best recomendation is " + bestItem);
			System.out.println("Owned by " + this.sumVector[this.membership[customer]][bestItem] + " out of " + this.members[this.membership[customer]] + " of members of this cluster");
			for (int j = 0; j < totalPrototypeVectors; j++) {
				for (int i = 0; i < maxItems; i++) {
				System.out.print(this.prototypeVector[j][i]);
				}
				System.out.println(" ");
			}
			for (int k = 0; k < maxCustomers; k++) {
				System.out.println(this.membership[k]);
			}
		} else {
			System.out.println("No recomendation can be made.");
		}
		System.out.println("Already owns: ");
	/*	for (item = 0; item < maxItems; item++) {
			if (database[customer][item] == 1) {
				System.out.println(this.itemName[item]);
			}
		}
		*/
	}
}


public class Cluster {
	public static void main(String[] args) {
		ArtData art = new ArtData();
		int customer;
		art.init();
		art.ArtAlgorythm();
		for (customer = 0; customer < ArtData.maxCustomers; customer++) {
			art.makeRecomendation(customer);
		}
	}
}
