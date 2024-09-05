package Histogramas;

// Registro de observaciones ponderadas

class HistogramWeighted {
	private double accumObs;
	private double totTime;
	private double lastTim;
	
	public HistogramWeighted() { // Inicializa el registro
		accumObs = 0;
		totTime = 0;
		lastTim = 0;
	}
	
	public void log(double time, double obs) { // Registra una observacion
		accumObs = accumObs + (time - lastTim)*obs;
		totTime = totTime + (time - lastTim);
		lastTim = time;
	}
		
	public double getMean() { // Retorna la media
		if (totTime == 0) {
			System.out.println("No hay observaciones"); 
			return 0;
		} else
			return accumObs/totTime;
	}
	
	void reset() { // Resetea el registro
		accumObs = 0;
		totTime = 0;
		lastTim = 0;	
	}
}
