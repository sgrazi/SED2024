package TallerReparaciones;

// Programa principal que simula el taller de reparaciones (clase que tiene la operación main).

class TallerReparaciones {
    public static void main(String[] args) {
        // Se crea un simulador con valores particulares para sus parámetros.
		// - 20 máquinas
		// - tiempo entre roturas igual a 60
		// - tiempo de desarmado igual a 5
		// - tiempo de reparación igual a 8
		// - 4 mecánicos
		// - 3 equipamientos
		Simulator sim = new Simulator (20, 60, 5, 8, 4, 3);
		
		// Se inicializa la simulación
		sim.init();
		
		// Se ejecuta la simulación durante 8 horas de trabajo
		sim.run(8*60);
		System.out.println("Fin de jornada a la hora " + sim.getTim());
		
		// Se cierra la operación
		sim.enOperacion = false;
		
		// Se continúa por un tiempo arbitrariamente alto para finalizar la reparación de las máquinas rotas
		sim.run(10000);
		System.out.println("Fin de reparaciones a la hora " + sim.getTim());
    }
}
