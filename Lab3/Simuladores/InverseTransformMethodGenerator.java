package Simuladores;

import java.util.ArrayList;
import java.util.Random;

// Para una variable aleatoria continua exponencial negativa
public class InverseTransformMethodGenerator {
    private double lambda;
    private Random random;
    private ArrayList<Double> generated;
    private boolean useAnti = false;
    private double antithetic;

    public InverseTransformMethodGenerator(double lambda, int seed) {
        this.lambda = lambda;
        this.random = new Random(seed);
        this.generated = new ArrayList<>();
    }

    public InverseTransformMethodGenerator(double lambda) {
        this.lambda = lambda;
        this.random = new Random();
        this.generated = new ArrayList<>();
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    public double generate() {
        double U = random.nextDouble();
        double value = -Math.log(U) / lambda;
        generated.add(value);
        return value;
    }

//    public double generate() {
//        if (useAnti) {
//            useAnti = false;
//            generated.add(antithetic);
//            return antithetic;
//        } else {
//            double U = random.nextDouble();
//            double value = -Math.log(U) / lambda;
//            antithetic = -Math.log(1 - U) / lambda;
//            useAnti = true;
//            generated.add(value);
//            return value;
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
