

public class Particle {

    double xActual;
    double yActual;

    double velocity;

    double xBest;
    double yBest;

    public Particle(double x, double y) {
        this.xActual = x;
        this.yActual = y;
        velocity = 0;
        xBest = x;
        yBest = y;
    }

    public void updateBest() {
        xBest = xActual;
        yBest = yActual;
    }

    public double getxActual() {
        return xActual;
    }

    public double getyActual() {
        return yActual;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getxBest() {
        return xBest;
    }

    public double getyBest() {
        return yBest;
    }

    public void setxActual(double xActual) {
        this.xActual = xActual;
    }

    public void setyActual(double yActual) {
        this.yActual = yActual;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setxBest(double xBest) {
        this.xBest = xBest;
    }

    public void setyBest(double yBest) {
        this.yBest = yBest;
    }
}
