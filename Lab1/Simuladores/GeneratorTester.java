package Simuladores;

import java.util.ArrayList;
import java.util.List;

public class GeneratorTester {

    public static void main(String[] args) {
        int numSamples = 1000;

        InverseTransformMethodGenerator exponentialGenerator = new InverseTransformMethodGenerator(2.0);
        double[] exponentialData = new double[numSamples];
        for (int i = 0; i < numSamples; i++) {
            exponentialData[i] = exponentialGenerator.generate();
        }

        BoxMullerGenerator normalGenerator = new BoxMullerGenerator();
        List<Double> normalDataList = new ArrayList<>();
        for (int i = 0; i < numSamples / 2; i++) { // Llamadas necesarias para obtener 1000 muestras
            double[] generated = normalGenerator.generate();
            normalDataList.add(generated[0]);
            normalDataList.add(generated[1]);
        }
        double[] normalData = normalDataList.stream().mapToDouble(Double::doubleValue).toArray();

        System.out.println("Exponential Data: ");
        printArray(exponentialData);

        System.out.println("\nNormal Data: ");
        printArray(normalData);
    }

    public static void printArray(double[] data) {
        System.out.print("[");
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i]);
            if (i < data.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}