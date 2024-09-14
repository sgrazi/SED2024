package Histogramas;

// Registro de tendencias de observaciones

class HistogramTendency {
	private double [] times;
	private double [] values;
	private int top;
	private int size;
	
	public HistogramTendency(int n) { // Inicializa el registro para una cantidad maxima de observaciones n
		times = new double[n];
		values = new double[n];
		size = n;
		top = 0;
	}
	
	public void log(double time, double value) { // Registra una observacion
		if (top == size)
			System.out.println("No hay espacio para registrar más observaciones"); 	
		else {
			times[top] = time;
			values[top] = value;
			top++;
		}
	}
	
	public void print() { // Imprime el contenido del registro
		System.out.println("Tiempo Valor");
		for (int i=0; i<top; i++)
			System.out.println(times[i] + " " + values[i]);
	}
	
	void reset() { // Resetea el registro
		top = 0;	
	}
}
