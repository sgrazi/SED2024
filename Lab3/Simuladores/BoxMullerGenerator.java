package Simuladores;

import java.util.ArrayList;
import java.util.Random;

// Para obtener muestras de una distribucion normal
public class BoxMullerGenerator {
    private Random random;
    private double sigma = 1;
    private double mu = 0;
    private ArrayList<Double> generated;
    private boolean useAnti = false;
    private double[] anti = new double[2];

    public BoxMullerGenerator(double sigma, double mu, int seed) {
        this.sigma = sigma;
        this.mu = mu;
        this.random = new Random(seed);
        this.generated = new ArrayList<>();
    }

    public BoxMullerGenerator(double sigma, double mu) {
        this.sigma = sigma;
        this.mu = mu;
        this.random = new Random();
        this.generated = new ArrayList<>();
    }

    public BoxMullerGenerator() {
        this.random = new Random();
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public double getMu() {
        return mu;
    }

    public void setMu(double mu) {
        this.mu = mu;
    }

    public double[] generate() {
        double U1 = random.nextDouble();
        double U2 = random.nextDouble();

        double X0 = Math.sqrt(-2.0 * Math.log(U1)) * Math.cos(2.0 * Math.PI * U2) * sigma + mu;
        double X1 = Math.sqrt(-2.0 * Math.log(U1)) * Math.sin(2.0 * Math.PI * U2) * sigma + mu;

        generated.add(X0);

        return new double[]{X0, X1}; // Devuelve dos muestras independientes de la normal estándar
    }

//    public double[] generate() {
//        if (useAnti) {
//            // Devuelve los valores antitéticos almacenados
//            useAnti = false; // Alterna a generar nuevos valores en la siguiente llamada
//            generated.add(anti[0]);
//            return anti;
//        } else {
//            double U1 = random.nextDouble();
//            double U2 = random.nextDouble();
//
//            double X0 = Math.sqrt(-2.0 * Math.log(U1)) * Math.cos(2.0 * Math.PI * U2) * sigma + mu;
//            double X1 = Math.sqrt(-2.0 * Math.log(U1)) * Math.sin(2.0 * Math.PI * U2) * sigma + mu;
//
//            anti[0] = Math.sqrt(-2.0 * Math.log(1 - U1)) * Math.cos(2.0 * Math.PI * (1 - U2)) * sigma + mu;
//            anti[1] = Math.sqrt(-2.0 * Math.log(1 - U1)) * Math.sin(2.0 * Math.PI * (1 - U2)) * sigma + mu;
//
//            useAnti = true;
//            generated.add(X0);
//            return new double[]{X0, X1}; // Devuelve dos muestras independientes de la normal estándar
//        }
//    }

    public double[] getStats() {
        double suma = 0;
        for (double valor : generated) {
            suma += valor;
        }
        double media = suma  / generated.size();

        double sumaCuadrados = 0;
        for (double valor : generated) {
            sumaCuadrados += Math.pow(valor - media, 2);
        }
        double varianza = sumaCuadrados / (generated.size() - 1);
        return new double[]{media, varianza};
    }
}
