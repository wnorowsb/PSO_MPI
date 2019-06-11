
public class Main {

    public static void main(String args[]){
        PolynomialEquation eq = new PolynomialEquation("example2.txt");
        Algorithm alg = new Algorithm(eq, -20, 12);
        alg.runAlgorithm(20,100,args);
    }
}