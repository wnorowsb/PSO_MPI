import mpi.MPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Algorithm {
    private double bestGlobal[];
    private double bestLocal[];
    private double mailBox[];
    private static double LOWER_BOUNDARY;
    private static double UPPER_BOUNDARY;
    private static final double W  = 0.6; //waga inercji
    private static final double C1 = 2.05; // współczynnik uczenia (kognitywny)
    private static final double C2 = 2.05; // współczynnik uczenia (socjalny)
    private Random rand;
    private PolynomialEquation eq;
    private List<Particle> Swarn,SwarnPart;

    public Algorithm(PolynomialEquation polynomialEquation, double lowerBoundary, double upperBoundary) {
        if (lowerBoundary >= upperBoundary)
            throw new IllegalArgumentException("Upper boundary must be higher than lower boundary!");

        LOWER_BOUNDARY = lowerBoundary;
        UPPER_BOUNDARY = upperBoundary;
        this.eq = polynomialEquation;
        rand = new Random();
    }

    public void runAlgorithm(int swarnSize, int iterationsNumber,String args[]){
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        Random rand = new Random(rank);
        int nswarns = swarnSize / size;
        double atom = (UPPER_BOUNDARY-LOWER_BOUNDARY)/ swarnSize;
        double newVelocity;
        double xPrevious;
        mailBox= new double [size*2];
        bestGlobal= new double []{0,99999};
        bestLocal = new double[]{0, 999999}; 
        Swarn = new ArrayList<>();
        SwarnPart = new ArrayList<>();
        //initialize particles in each process
        for(double i = rank*nswarns; i<(rank*nswarns)+nswarns;i++){
            SwarnPart.add(new Particle(LOWER_BOUNDARY +i*atom,eq.countValue(LOWER_BOUNDARY +i*atom))); 
        }
        
        for(int i=0; i<iterationsNumber; i++) {
            for(Particle p : SwarnPart){
                if(p.getyActual() < p.getyBest()) {
                    p.setyBest(p.getyActual());
                    p.setxBest(p.getxActual());
                }
                if(p.getyBest()<bestLocal[1]) {
                    bestLocal[0]=p.getxBest();
                    bestLocal[1] = p.getyBest();
                }
            }
            //gathers local best solutions and change global best if nessesary
            MPI.COMM_WORLD.Gather(bestLocal, 0, 2, MPI.DOUBLE, mailBox, 0, 2, MPI.DOUBLE, 0);
            if (rank == 0) {
                for (int j = 0; j < mailBox.length -1; j = j + 2) {
                    if (mailBox[j+1] < bestGlobal[1]) {
                        bestGlobal[0] = mailBox[j];
                        bestGlobal[1] = mailBox[j+1];
                    }
                }
            }
            //send global best to all processes
            MPI.COMM_WORLD.Bcast(bestGlobal, 0, 2, MPI.DOUBLE, 0);
            //moves particles
            for (Particle p : SwarnPart) {
                xPrevious = p.getxActual();
                newVelocity = W * p.getVelocity()
                        + C1 * rand.nextDouble() * (p.getxBest() - xPrevious)
                        + C2 * rand.nextDouble() * (bestGlobal[0] - xPrevious);
                p.setVelocity(newVelocity);
                p.setxActual(xPrevious + newVelocity);
                p.setyActual(eq.countValue(p.getxActual()));
            }

            if(rank==0) {
                System.out.println("Iteracja " +i+ " najlepsze znalezione rozwiązanie: x: " + bestGlobal[0]+" y: "+bestGlobal[1]);
            }
        }
        MPI.Finalize();
    }
}
