package TallerReparaciones;

// Clase que tiene el motor de simulación y los datos y operaciones correspondientes al modelo que se simula.

import java.util.*; // Para utilizar algunas construcciones de Java como List y Queue.

class Simulator {
	// Atributos del simulador
	private double tim; // Tiempo actual de la simulación
	private Entity current; // Entidad que ejecuta el evento B en el tiempo actual
	private List<Entity> calendar; // El calendario

	// Atributos del modelo
	double tiempoEntreRoturas, tiempoDesarmar, tiempoReparar;
	int cantMaquinas, cantMecanicos, cantEquipamiento, dispMecanicos, dispEquipamiento;
	boolean enOperacion;
	Queue<Entity> q1, q2;

	// Operaciones del simulador
	Simulator(int cantMaquinas, double tiempoEntreRoturas, int tiempoDesarmar, int tiempoReparar, int cantMecanicos, int cantEquipamiento) {
		// Setea parámetros
		this.cantMaquinas = cantMaquinas;
		this.tiempoEntreRoturas = tiempoEntreRoturas;
		this.tiempoDesarmar = tiempoDesarmar;
		this.tiempoReparar = tiempoReparar;
		this.cantMecanicos = cantMecanicos;
		this.cantEquipamiento = cantEquipamiento;
	}

	public double getTim() {
		return tim;
	}

	public void imprimirCalendario() {	
		// Imprime el calendario en pantalla
		System.out.println("Contenido del calendario [evento, entidad, tiempo]:");
		for (int i=0; i<calendar.size(); i++) 
			System.out.println("[" + calendar.get(i).getNextBEvent() + ", " + calendar.get(i).getId() + ", " + calendar.get(i).getTime() + "]");
		System.out.println("En cola Q1: " + q1.size());
		System.out.println("En cola Q2: " + q2.size());
		System.out.println("Mecánicos en uso: " + (cantMecanicos - dispMecanicos));
		System.out.println("Equipamiento en uso: " + (cantEquipamiento - dispEquipamiento));
	}
	
	public void cause(int bEvent, Entity e, double time) {
		// Operación que agenda un evento B en el calendario
		
		// Setea en la entidad el evento fijo y el instante de tiempo en el que se debe agendar
		e.setNextBEvent(bEvent);
		e.setTime(tim + time);

		// Inserta la entidad en el lugar apropiado del calendario según su reloj
		if (calendar.size() == 0)
			calendar.add(0, e);
		else {
			int i;
			for (i=0; i<calendar.size(); i++) {
				if (calendar.get(i).getTime() > e.getTime()) {
					calendar.add(i, e);
					break;
				}
			}
			if (i == calendar.size())
				calendar.add(calendar.size(), e);
		}
	}
	
	void init() {
		// Operación que inicializa la simulación
		
		// Inicializa variables del simulador
		tim = 0;
		current = null;
		calendar = new LinkedList<Entity>();
		
		// Inicializa colas, recursos y variables del modelo
		q1 = new LinkedList<Entity>();
		q2 = new LinkedList<Entity>();
		dispMecanicos = cantMecanicos;
		dispEquipamiento = cantEquipamiento;
		enOperacion = true;
		
		// Agenda primera rotura para todas las máquinas
		for (int i=1; i<=cantMaquinas; i++)
			cause(1, new Entity(i), tiempoEntreRoturas);		
	}
	
	public void run(double duration) {
		// Motor de la simulación, ejecuta las tres fases
		
		while (!calendar.isEmpty() && (tim < duration)) {
			// Fase A: avance del tiempo
			tim = calendar.get(0).getTime();
			
			if (tim > duration)
				break; // Finaliza la simulacion
			
			// Fase B: ejecucion de todos los eventos B agendados para ese tiempo
			while (!calendar.isEmpty() && calendar.get(0).getTime() == tim) {				
				// Extrae la primera entidad del calendario y la coloca en current
				current = calendar.remove(0);
			
				// Ejecuta el evento B que corresponde
				switch (current.getNextBEvent()) {
					case 1:
						B1();
						break;
					case 2:
						B2();
						break;
					case 3:
						B3();
						break;
					// Agregar tantos case como eventos B tenga el modelo
					/*
					case i:
						Bi();
						break;
					*/				
				}
				imprimirCalendario();
			}

			// Fase C: ejecucion de eventos C (si se cumplen sus respectivas condiciones)
			C1();
			imprimirCalendario();
			C2();
			imprimirCalendario();
			// Agregar tantos Ci como eventos condicionados tenga el modelo
			// Ci();
			// imprimirCalendario();
		}
	}
	
	// Operaciones del modelo
	private void B1 () { 
		if (enOperacion) {
			// Final de funcionamiento (rotura)
			System.out.println("Se rompe la máquina " + current.getId() + " a la hora " + tim);
			q1.add(current); // Se agrega la entidad actual a la cola de espera q1 (por un mecánico)
		}
	}
	
	private void B2 () { 
		// Evento final de desarmar
		System.out.println("Finaliza desarmado de máquina " + current.getId() + " a la hora " + tim);
		q2.add(current); // Se agrega la entidad actual a la cola de espera q2 (por equipamiento)
	}

	private void B3 () { 
		// Evento final de reparar
		System.out.println("Finaliza reparación de máquina " + current.getId() + " a la hora " + tim);
		
		// Devuelve mecánico y equipamiento
		dispMecanicos++;
		dispEquipamiento++;
		if (enOperacion)
			cause(1, current, tiempoEntreRoturas); // Se agenda la próxima rotura para la máquina current
	}

	private void C1() { 
		// Evento comienzo desarmar máquina
		while (dispMecanicos > 0 && !q1.isEmpty()) {
			System.out.println("Comienza desarmado de máquina " + q1.peek().getId() + " a la hora " + tim);
			dispMecanicos--; // Se toma una unidad del recurso mecánicos
			cause(2, q1.remove(), tiempoDesarmar); // Se agenda el final del desarmado de la máquina que estaba en q1
		}
	}
	
	private void C2() { 
		// Evento comienzo reparación de máquina
		while (dispEquipamiento > 0 && !q2.isEmpty()) {
			System.out.println("Comienza reparación de máquina " + q2.peek().getId() + " a la hora " + tim);
			dispEquipamiento--; // Se toma una unidad del recurso equipamiento
			cause(3, q2.remove(), tiempoReparar); // Se agenda el final de la reparación de la máquina que estaba en q2
		}
	}
}
