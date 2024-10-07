package TallerReparaciones;

// Clase que tiene el motor de simulación y los datos y operaciones correspondientes al modelo que se simula.

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import Histogramas.HistogramWeighted;
import Simuladores.*;

class Simulator {
	// Atributos del simulador
	private double tim; // Tiempo actual de la simulación
	private Entity current; // Entidad que ejecuta el evento B en el tiempo actual
	private List<Entity> calendar; // El calendario

	// Atributos del modelo
	double tiempoEntreRoturas;
	int cantMaquinas, cantMecanicos, cantEquipamiento, dispMecanicos, dispEquipamiento, cantMaquinasRotas;
	boolean enOperacion;
	Queue<Entity> q1, q2;

	// Generadores de numeros aleatorios
	InverseTransformMethodGenerator generadorTiemposEntreRoturas;
	BoxMullerGenerator generadorTiemposDesarmado;
	BoxMullerGenerator generadorTiemposReparado;

	// Histogramas
	private HistogramWeighted mecanicosUtilizados;
	private HistogramWeighted equipamientoUtilizado;
	private HistogramWeighted equipamientoRoto;

	// Listas para almacenar los valores generados
	private List<Double> tiemposEntreRoturas = new LinkedList<Double>();
	private List<Double> tiemposDesarmado = new LinkedList<Double>();
	private List<Double> tiemposReparado = new LinkedList<Double>();
	private List<Double> tiemposEsperaMecanicos = new LinkedList<>();
	private List<Double> tiemposEsperaEquipamiento = new LinkedList<>();


	// Operaciones del simulador
	Simulator(int cantMaquinas, double tiempoEntreRoturas, int cantMecanicos, int cantEquipamiento) {
		// Setea parámetros
		this.cantMaquinas = cantMaquinas;
		this.tiempoEntreRoturas = tiempoEntreRoturas;
		this.cantMecanicos = cantMecanicos;
		this.cantEquipamiento = cantEquipamiento;
		this.cantMaquinasRotas = 0;

		this.generadorTiemposEntreRoturas = new InverseTransformMethodGenerator(1/tiempoEntreRoturas);
		this.generadorTiemposDesarmado = new BoxMullerGenerator(1,5);
		this.generadorTiemposReparado = new BoxMullerGenerator(2,8);

		this.mecanicosUtilizados = new HistogramWeighted();
		this.equipamientoUtilizado = new HistogramWeighted();
		this.equipamientoRoto = new HistogramWeighted();
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
		for (int i=1; i<=cantMaquinas; i++) {
			double tiempo = generadorTiemposEntreRoturas.generate();
			tiemposEntreRoturas.add(Math.floor(tiempo));
			cause(1, new Entity(i), Math.floor(tiempo));
		}
	}
	
	public double[] run(double duration) {
		// para retornar las medias de cada simulacion
		double[] means = new double[3];

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
//				imprimirCalendario();
			}


			// Fase C: ejecucion de eventos C (si se cumplen sus respectivas condiciones)
			C1();
//			imprimirCalendario();
			C2();
//			imprimirCalendario();
			// Agregar tantos Ci como eventos condicionados tenga el modelo
			// Ci();
			// imprimirCalendario();

			equipamientoUtilizado.log(tim, cantEquipamiento - dispEquipamiento);
			mecanicosUtilizados.log(tim, cantMecanicos - dispMecanicos);
			equipamientoRoto.log(tim, cantMaquinasRotas);
		}

		// imprimirValoresGenerados();
		//imprimirHistogramas();
		imprimirTiemposEspera();

		means[0] = mecanicosUtilizados.getMean();
		means[1] = equipamientoUtilizado.getMean();
		means[2] = equipamientoRoto.getMean();
		return means;
	}
	
	// Operaciones del modelo
	private void B1 () { 
		if (enOperacion) {
			// Final de funcionamiento (rotura)
			cantMaquinasRotas++;
//			System.out.println("Se rompe la máquina " + current.getId() + " a la hora " + tim);
			current.marcarInicioEspera(tim); // inicio de la espera mecanico
			q1.add(current); // Se agrega la entidad actual a la cola de espera q1 (por un mecánico)
		}
	}
	
	private void B2 () { 
		// Evento final de desarmar
//		System.out.println("Finaliza desarmado de máquina " + current.getId() + " a la hora " + tim);
		current.marcarInicioEspera(tim); // inicio de la espera equipamiento
		q2.add(current); // Se agrega la entidad actual a la cola de espera q2 (por equipamiento)
	}

	private void B3 () { 
		// Evento final de reparar
//		System.out.println("Finaliza reparación de máquina " + current.getId() + " a la hora " + tim);
		
		// Devuelve mecánico y equipamiento
		dispMecanicos++;
		dispEquipamiento++;
		cantMaquinasRotas--;
		if (enOperacion) {
			double tiempo = generadorTiemposEntreRoturas.generate();
			tiemposEntreRoturas.add(Math.floor(tiempo));
			cause(1, current, Math.floor(tiempo)); // Se agenda la próxima rotura para la máquina current
		}
	}

	private void C1() { 
		// Evento comienzo desarmar máquina
		while (dispMecanicos > 0 && !q1.isEmpty()) {
			Entity e = q1.peek();
			tiemposEsperaMecanicos.add(tim - e.inicioEspera); // finaliza espera mecanico
//			System.out.println("Comienza desarmado de máquina " + q1.peek().getId() + " a la hora " + tim);
			dispMecanicos--; // Se toma una unidad del recurso mecánicos
			double[] tiempoDesarmado = generadorTiemposDesarmado.generate();
			tiemposDesarmado.add(Math.floor(tiempoDesarmado[0]));
			cause(2, q1.remove(), Math.floor(tiempoDesarmado[0])); // Se agenda el final del desarmado de la máquina que estaba en q1
		}
	}
	
	private void C2() { 
		// Evento comienzo reparación de máquina
		while (dispEquipamiento > 0 && !q2.isEmpty()) {
			Entity e = q2.peek();
			tiemposEsperaEquipamiento.add(tim - e.inicioEspera);// finaliza espera equipamiento
//			System.out.println("Comienza reparación de máquina " + q2.peek().getId() + " a la hora " + tim);
			dispEquipamiento--; // Se toma una unidad del recurso equipamiento
			double[] tiempoReparado = generadorTiemposReparado.generate();
			tiemposReparado.add(Math.floor(tiempoReparado[0]));
			cause(3, q2.remove(), Math.floor(tiempoReparado[0])); // Se agenda el final de la reparación de la máquina que estaba en q2
		}
	}

	private void imprimirValoresGenerados() {
		// Imprimir valores generados para graficar en Python
		System.out.println("Tiempos entre roturas: " + tiemposEntreRoturas);
		System.out.println("Tiempos de desarmado: " + tiemposDesarmado);
		System.out.println("Tiempos de reparado: " + tiemposReparado);
	}

	private void imprimirHistogramas() {
		System.out.println("Cantidad promedio de mecanicos utilizados: " + mecanicosUtilizados.getMean());
		System.out.println("Cantidad promedio de equipamiento utilizado: " + equipamientoUtilizado.getMean());
		System.out.println("Cantidad promedio de equipamiento roto: " + equipamientoRoto.getMean());
	}

	private void imprimirTiemposEspera() {
		double aux = 0;
		for (int i=0; i < tiemposEsperaMecanicos.size(); i++){
			aux += tiemposEsperaMecanicos.get(i);
		}
		System.out.println("Tiempo medio espera por mecanicos: " + aux/tiemposEsperaEquipamiento.size());

		aux = 0;
		for (int i=0; i < tiemposEsperaEquipamiento.size(); i++){
			aux += tiemposEsperaEquipamiento.get(i);
		}
		System.out.println("Tiempo medio espera por equipamiento: " + aux/tiemposEsperaEquipamiento.size());
	}
}
