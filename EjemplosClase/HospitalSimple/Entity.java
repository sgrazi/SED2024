package HospitalSimple;

// Clase que representa una entidad, con su identificador, el número del siguiente evento B en el que participa y el instante de tiempo en el que ocurre ese evento.

class Entity {
	private int id;
	private int nextBEvent;
	private double time;

	public Entity(int id) {
		this.id = id;
		this.nextBEvent= 0;
		this.time = 0;
	}
	
	public int getId() {
		return this.id;
	}

	public int getNextBEvent() {
		return this.nextBEvent;
	}

	public double getTime() {
		return this.time;
	}
	
	void setNextBEvent(int nextBEvent) {
		this.nextBEvent = nextBEvent;
	}
	
	void setTime(double time) {
		this.time = time;

	}
}
