package Player;
public class Player {
	public String name;
	public String position;
	public int salary;
	public String team;
	public double value;
	public double cons;
	public double score;
	public int game;
	public String start;
	

	// for 3,4 game slate Optimizer
	public Player(String name, String position, int salary, String team,
			double value, double cons, int game, String start,double score) {
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.team = team;
		this.value = value;
		this.cons = cons;
		this.game = game;
		this.start = start;
		this.score = score;
	}
	
	// for Main Optimizer
	public Player(String name, String position, int salary, String team,
			double value, double score) {
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.team = team;
		this.value = value;
		this.score = score;
	}
	
	
	public Player(String name, String position, int salary, String team,
			double value, double cons, int game) {
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.team = team;
		this.value = value;
		this.cons = cons;
		this.game = game;
	}
	
	public Player(String name, String position, int salary, String team,
			double value, double cons, int game, String start) {
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.team = team;
		this.value = value;
		this.cons = cons;
		this.game = game;
		this.start = start;
	}
}