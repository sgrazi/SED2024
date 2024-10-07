package TallerReparaciones;

// Programa principal que simula el taller de reparaciones (clase que tiene la operación main).

class TallerReparaciones {
    public static void main(String[] args) {
		int N = 30;
		double[] means = new double[N*3];
		for (int n = 0 ; n < N; n++) {
			// Se crea un simulador con valores particulares para sus parámetros.
			// - 20 máquinas
			// - tiempo entre roturas igual a 60
			// - tiempo de desarmado igual a 5
			// - tiempo de reparación igual a 8
			// - 4 mecánicos
			// - 3 equipamientos
			Simulator sim = new Simulator (20, 60, 4, 3);

			// Se inicializa la simulación
			sim.init();

			// Se ejecuta la simulación durante 8 horas de trabajo
			double[] a = sim.run(8 * 60);
			System.out.println("Fin de jornada a la hora " + sim.getTim());

			// Se cierra la operación
			sim.enOperacion = false;

			// Se continúa por un tiempo arbitrariamente alto para finalizar la reparación de las máquinas rotas
			double[] m = sim.run(10000);
			means[n*3] = m[0];
			means[n*3 + 1] = m[1];
			means[n*3 + 2] = m[2];
			System.out.println("Fin de reparaciones a la hora " + sim.getTim());
		}

		System.out.println("Medias de las " + N + " ejecuciones");
		for (int i = 0 ; i < 3; i++) {
			System.out.print("[");
			for (int j = 0 ; j < N; j++) {
				System.out.print(means[(j*3)+i] + ", ");
			}
			System.out.println("]");
		}
    }
}