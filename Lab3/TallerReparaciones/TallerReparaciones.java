package TallerReparaciones;

// Programa principal que simula el taller de reparaciones (clase que tiene la operación main).

import java.util.Arrays;

class TallerReparaciones {
    public static void main(String[] args) {
		int N = 100;
		double[] stats = new double[N*6];
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

			// Se cierra la operación
			sim.enOperacion = false;

			// Se continúa por un tiempo arbitrariamente alto para finalizar la reparación de las máquinas rotas
			double[] m = sim.run(10000);
			System.arraycopy(m, 0, stats, n*6, 6);
		}

		// Imprimir stats
		double[] sumas = new double[6];

		// Calcula las sumas y los conteos
		for (int i = 0; i < stats.length; i++) {
			sumas[i % 6] += stats[i];
		}
		System.out.println("Promedio generador roturas " + sumas[0]/N);
		System.out.println("Varianza generador roturas " + sumas[1]/N);
		System.out.println("Promedio generador desarmado " + sumas[2]/N);
		System.out.println("Varianza generador desarmado " + sumas[3]/N);
		System.out.println("Promedio generador reparado " + sumas[4]/N);
		System.out.println("Varianza generador reparado " + sumas[5]/N);
    }
}
