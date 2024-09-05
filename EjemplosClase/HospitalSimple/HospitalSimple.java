package HospitalSimple;

// Programa principal que simula el hospital simple (clase que tiene la operación main).

class HospitalSimple {
    public static void main(String[] args) {
        // Se crea un simulador con llegadas de pacientes cada 5 unidades de tiempo, estadia en el hospital de 20 unidades de tiempo y disponibilidad de recursos de 4 camas.
		Simulator sim = new Simulator (5, 20, 4);
		
		// Se inicializa la simulación.
		sim.init();
		
		// Se ejecuta la simulación durante 1000 unidades de tiempo.
		sim.run(1000);
    }
}
