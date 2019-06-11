import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PolynomialEquation {

    double [] coefficients;

    public PolynomialEquation(String path) {
        coefficients = readFile(path);
        for (double a : coefficients) {
           // System.out.println(a);
        }
    }

    public double countValue(double x){
        double result =  0;
        for(int i = 0; i < coefficients.length; i++)
            result += coefficients[i] * Math.pow(x, i);
        return result;
    }

    private double [] readFile(String path){
        File file = new File(path);
        try {
            Scanner sc = new Scanner(file);
            List<Double> tmp = new ArrayList<>();
            while(sc.hasNextDouble()){
                tmp.add(sc.nextDouble());
            }
            return tmp.stream().mapToDouble(d -> d).toArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
