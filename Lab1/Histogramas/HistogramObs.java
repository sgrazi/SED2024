package Histogramas;

// Registro de observaciones

class HistogramObs {
	private double accumObs;
	private double totObs;
	
	public HistogramObs() { // Inicializa el registro
		accumObs = 0;
		totObs = 0;
	}
	
	public void log(double obs) { // Registra una observacion
		accumObs = accumObs + obs;
		totObs = totObs + 1;
	}
		
	public double getMean() { // Retorna la media
		if (totObs == 0) {
			System.out.println("No hay observaciones"); 
			return 0;
		} else
			return accumObs/totObs;
	}

	void reset() { // Resetea el registro
		accumObs = 0;
		totObs = 0;	
	}
}
