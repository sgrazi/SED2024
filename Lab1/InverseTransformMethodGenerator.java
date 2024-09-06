import java.util.Random;

// Para una variable aleatoria continua exponencial negativa
public class InverseTransformMethodGenerator {
    private double lambda;
    private Random random;

    public InverseTransformMethodGenerator(double lambda, int seed) {
        this.lambda = lambda;
        this.random = new Random(seed);
    }

    public InverseTransformMethodGenerator(double lambda) {
        this.lambda = lambda;
        this.random = new Random();
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }

    // Transformacion inversa
    // U = F(X) -> X = F^(-1)(U)
    // U = F(X) = e^(-λX) -> X = -1/λ ln(U)
    public double generate() {
        double lambdaValue = lambda;
        double U = random.nextDouble();
        return -Math.log(U) / lambdaValue;
    }
}
