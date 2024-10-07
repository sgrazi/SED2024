package Simuladores;

import java.util.Random;

// Para obtener muestras de una distribucion normal
public class BoxMullerGenerator {
    private Random random;
    private double sigma = 1;
    private double mu = 0;

    public BoxMullerGenerator(double sigma, double mu, int seed) {
        this.sigma = sigma;
        this.mu = mu;
        this.random = new Random(seed);
    }

    public BoxMullerGenerator(double sigma, double mu) {
        this.sigma = sigma;
        this.mu = mu;
        this.random = new Random();
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

        return new double[]{X0, X1}; // Devuelve dos muestras independientes de la normal estándar
    }

}
