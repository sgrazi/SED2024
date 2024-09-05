package HospitalSimple;

// Clase que tiene el motor de simulación y los datos y operaciones correspondientes al modelo que se simula.

import java.util.*; // Para utilizar algunas construcciones de Java como List y Queue.

class Simulator {
	// Atributos del simulador
	private double tim; // Tiempo actual de la simulación
	private Entity current; // Entidad que ejecuta el evento B en el tiempo actual
	private List<Entity> calendar; // El calendario

	// Atributos del modelo
	double tiempoEntreArribos, tiempoEstadia;
	int maxCamas, dispCamas, idConsecutivo;
	Queue<Entity> q1;

	// Operaciones del simulador
	Simulator(double tiempoEntreArribos, int tiempoEstadia, int maxCamas) {
		// Setea parametros
		this.tiempoEntreArribos = tiempoEntreArribos;
		this.tiempoEstadia = tiempoEstadia;
		this.maxCamas = maxCamas;
	}

	public void imprimirCalendario() {	
		// Imprime el calendario en pantalla
		System.out.println("Contenido del calendario:");
		for (int i=0; i<calendar.size(); i++) 
			System.out.println("[" + calendar.get(i).getNextBEvent() + ", " + calendar.get(i).getId() + ", " + calendar.get(i).getTime() + "]");
		System.out.println("En cola: " + q1.size());
		System.out.println("En cama: " + (maxCamas - dispCamas));
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
		dispCamas = maxCamas;
		idConsecutivo = 1;

		// Agenda un evento para que el calendario tenga contenido inicial
		cause(1, new Entity(idConsecutivo), 0);		
	}
	
	public void run(double duration) {
		// Motor de la simulación, ejecuta las tres fases
		
		while (!calendar.isEmpty() && (tim < duration)) {
			// Fase A: avance del tiempo
			tim = calendar.get(0).getTime();
			
			if (tim > duration)
				break; // Finaliza la simulacion
			
			// Fase B: ejecucion de todos los eventos B agendados para ese tiempo
			while (calendar.get(0).getTime() == tim) {
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
			// Agregar tantos Ci como eventos condicionados tenga el modelo
			// Ci();
			// imprimirCalendario();
		}
	}
	
	// Operaciones del modelo
	private void B1 () { 
		// Evento arribo de paciente
		System.out.println("Arriba el paciente " + current.getId() + " a la hora " + tim);
		q1.add(current); // Se agrega la entidad actual a la cola de espera q1
		idConsecutivo++;	
		cause(1, new Entity(idConsecutivo), tiempoEntreArribos); // Se agenda el próximo arribo (nueva entidad)
	}
	
	private void B2 () { 
		// Evento fin de atencion de paciente
		System.out.println("Egresa el paciente " + current.getId() + " a la hora " + tim);
		dispCamas++; // Se devuelve una entidad del recurso camas
	}

	private void C1() { 
		// Evento comienzo de atencion de paciente
		while (dispCamas > 0 && !q1.isEmpty()) {
			System.out.println("Comienza internacion el paciente " + q1.peek().getId() + " a la hora " + tim);
			dispCamas--; // Se toma una unidad del recurso camas
			cause(2, q1.remove(), tiempoEstadia); // Se agenda el final de la estadía con la entidad que estaba primera en la cola q1
		}
	}
}
