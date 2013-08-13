import java.util.Random;

class Const {
	final static int maxCities = 30;
	final static int maxDistance = 100;
	final static int maxTour = maxCities * maxDistance;
	final static int maxAnts = maxCities;
	
}
class City {
	int x;
	int y;
	City () {
		this.x=0;
		this.y=0;
	}
}
class Ant {
	int curCity;
	int nextCity;
	int[] tabu = new int[Const.maxCities];   //char
	int pathIndex;
	int[] path = new int[Const.maxCities];   //char
	double tourLength;	
}

class Param {
	final static double alpha = 1.0;
	final static double beta = 5.0;
	final static double rho = 0.5;
	final static int qval = 100;
	final static int maxTours = 20;
	final static int maxTime = maxTours * Const.maxCities;
	final static double initPheromone = (1.0 / Const.maxCities);
}
class Method {
	//Инициализация рабочего пространства
	City[] cities = new City[Const.maxCities];
	Ant[] ants = new Ant[Const.maxCities];
	Method () {
		for (int i = 0; i < Const.maxCities; i++) {
			this.cities[i] = new City();
			this.ants[i] = new Ant();
			//for (int j=0; j < Const.maxCities; j++) {
				//this.ants[i].tabu[j] = new 
			//}
		}
	}
	double[][] distance = new double [Const.maxCities][Const.maxCities];
	double[][] pheromone = new double [Const.maxCities][Const.maxCities];
	double best = (double)Const.maxTour;
	int bestIndex;
	
	public void init() {
		Random rand = new Random();
		int from, to, ant;
		
		for (from = 0; from < Const.maxCities; from++) {
			//Расположение городов случайным образом
			this.cities[from].x = rand.nextInt(Const.maxDistance);
			this.cities[from].y = rand.nextInt(Const.maxDistance);
			
			for (to = 0; to < Const.maxCities; to++) {
				this.distance[from][to] = 0.0;
				this.pheromone[from][to] = Param.initPheromone;
			}
		}
		for (from = 0; from < Const.maxCities; from++) {
			for (to = 0; to < Const.maxCities; to++) {
				if ((to != from) && (this.distance[from][to] == 0.0)) {
					int xd = Math.abs(this.cities[from].x - this.cities[to].x);
					int yd = Math.abs(this.cities[from].y - this.cities[to].y);
					
					this.distance[from][to] = Math.sqrt((xd*xd) + (yd*yd));
					this.distance[to][from] = this.distance[from][to];
				}
			}
		}
		
		//Инициализация муравьев
		to = 0;
		for (ant = 0; ant < Const.maxAnts; ant++) {
			if (to == Const.maxCities) {
				to = 0;
			}
			this.ants[ant].curCity = to++;
			for (from = 0; from < Const.maxCities; from++) {
				this.ants[ant].tabu[from] = 0;
				this.ants[ant].path[from] = -1;
			}
			this.ants[ant].pathIndex = 1;
			this.ants[ant].path[0] = ants[ant].curCity;
			this.ants[ant].nextCity = -1;
			this.ants[ant].tourLength = 0.0;
			//Помещаем текущий город в список табу
			this.ants[ant].tabu[ants[ant].curCity] = 1;
		}
	}
	
	public void restartAnts() {
		int ant, i, to = 0;
		for (ant =0; ant < Const.maxAnts; ant++) {
			if (this.ants[ant].tourLength < best) {
				this.best = this.ants[ant].tourLength;
				this.bestIndex = ant;
			}
			
			this.ants[ant].nextCity = -1;
			this.ants[ant].tourLength = 0.0;
			
			for (i = 0; i < Const.maxCities; i++) {
				this.ants[ant].tabu[i] = 0;
				this.ants[ant].path[i] = -1;
			}
			
			if (to == Const.maxCities) {
				to = 0;
			}
			this.ants[ant].curCity = to++;
			this.ants[ant].pathIndex = 1;
			this.ants[ant].path[0] = this.ants[ant].curCity;
			
			this.ants[ant].tabu[this.ants[ant].curCity] = 1;
		}
	}
	
	private double antProduct (int from, int to) {
		return ((Math.pow(this.pheromone[from][to], Param.alpha) * Math.pow((1.0 / this.distance[from][to]), Param.beta)));
	}
	
	public int selectNextCity (int ant) {
		int from, to;
		double denom = 0.0;
		Random rand = new Random();
		from = this.ants[ant].curCity;
		for (to = 0; to < Const.maxCities; to++) {
			if (this.ants[ant].tabu[to] == 0) {
				denom += this.antProduct(from, to);
			}
		}
		assert(denom != 0.0);
		do {
			double p;
			to++;
			if (to >= Const.maxCities) {
				to = 0;
			}
			if (this.ants[ant].tabu[to] == 0) {
				p = this.antProduct(from, to) / denom;
				if (rand.nextDouble() < p) {
					break;
				}
			}
		} while (true);
		
		return to;
	}
	
	public int simulateAnts() {
		int k;
		int moving = 0;
		for (k = 0; k< Const.maxAnts; k++) {
			if (this.ants[k].pathIndex < Const.maxCities) {
				this.ants[k].nextCity = this.selectNextCity(k);
				this.ants[k].tabu[this.ants[k].nextCity] = 1;
				this.ants[k].path[this.ants[k].pathIndex++] = this.ants[k].nextCity;
				this.ants[k].tourLength += this.distance[this.ants[k].curCity][this.ants[k].nextCity];
				if (this.ants[k].pathIndex == Const.maxCities) {
					this.ants[k].tourLength += this.distance[this.ants[k].path[Const.maxCities-1]][this.ants[k].path[0]];
				}
				this.ants[k].curCity = this.ants[k].nextCity;
				moving++;
			}
		}
		return moving;
	}
	
	public void updateTrails() {
		int from, to, i, ant;
		for (from = 0; from <Const.maxCities; from++) {
			for (to = 0; to < Const.maxCities; to++) {
				if (from != to) {
					this.pheromone[from][to] *= (1.0 - Param.rho);
					if  (this.pheromone[from][to] < 0.0) {
						this.pheromone[from][to] = Param.initPheromone;
					}
				}
			}
		}
		for (ant=0; ant < Const.maxCities; ant++) {
			for(i = 0; i < Const.maxCities; i++) {
				if (i < Const.maxCities-1) {
					from = this.ants[ant].path[i];
					to = this.ants[ant].path[i+1];
				} else {
					from = this.ants[ant].path[i];
					to = this.ants[ant].path[0];
				}
				this.pheromone[from][to] += (Param.qval / this.ants[ant].tourLength);
				this.pheromone[to][from] = this.pheromone[from][to];
			}
		}
		for (from = 0; from < Const.maxCities; from++) {
			for (to = 0; to < Const.maxCities; to++) {
				this.pheromone[from][to] *= Param.rho;
			}
		}
	}
	
}
public class AntALg {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Method space = new Method();
		int curTime = 0;
		space.init();
		while (curTime++ < Param.maxTime) {
			if (space.simulateAnts() == 0) {
				space.updateTrails();
				if  (curTime != Param.maxTime) {
					space.restartAnts();
				}
				System.out.println("CurTime is " + curTime + ", best is " + space.best);
			}
		}
		System.out.println ("best tour " + space.best);
		
		
	}

}
