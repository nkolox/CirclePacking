package Genetic;

import main.Circles;
import main.Coordinate;

/**
 * Created by LocalAdm on 12.2016.
 */
public class Individual {
    static int defaultGeneLength = 9;

    private double fitness = 0;
    private boolean isFitnessChanged = true;
    int r = 80;


    /* Circles and its params */
    private Circles circles;
    int wp = 1000;
    Coordinate genes[]; //origos

//    //ZA Tutorial TSP
//    public Individual(ArrayList<Circles> circlesList){
//        this.circlesList.addAll(circlesList);
//        Collections.shuffle(this.circlesList);
//
//    }

    public Individual(){

    }
    public Individual(int length){
       genes = new Coordinate[length];
    }

    public Individual initializeIndividual(){
        double x, y;
        circles = new Circles(genes.length, r);

        for(int i=0; i < this.genes.length; ++i) {
            x=wp*Math.random();
            y=wp*Math.random();
            this.genes[i] = new Coordinate(x, y);
        }
        return this;
    }

    public Coordinate[] randomIndividual(){
        double x, y;
        for(int i=0; i<=(circles.n-1); ++i) {
            x=wp*Math.random();
            y=wp*Math.random();
            genes[i] = new Coordinate(x, y);
        }
        return genes;
    }

    public static void setDefaultGeneLength(int length){
        defaultGeneLength = length;
    }

    public Coordinate getGene(int id){
        return genes[id];
    }

    public Coordinate[] getGenes(){
        isFitnessChanged = true;
        return genes;
    }

    public void setGene(int index, Coordinate value) {
        genes[index] = value;
        fitness = 0;
    }

    public Circles getCircles(){
        return circles;
    }

    public void setCircles(Circles c){
        circles = c;
    }

    /* Public methods */
    public int size() {
        return genes.length;
    }

    public double getFitness() {
        if (isFitnessChanged) {
            fitness = optimizedFunction();
            isFitnessChanged = false;
        }
        return fitness;
    }

    public double optimizedFunction(){

        circles.r = (minimumDistance(genes)/2.0);
        return circles.getSurface();
    }

    public double minimumDistance(Coordinate points[]) {
        double minDist;
        double dist;

        minDist=Math.sqrt(((points[0].x-points[1].x)*(points[0].x-points[1].x))
                +((points[0].y-points[1].y)*(points[0].y-points[1].y)));
        for(int i=0; i<=(genes.length-1); ++i) {
            for(int j=0; j<=(genes.length-1); ++j) {
                if(i != j) {
                    dist=Math.sqrt(((points[i].x-points[j].x)*(points[i].x-points[j].x))
                            +((points[i].y-points[j].y)*(points[i].y-points[j].y)));
                    if(dist<minDist)  { minDist=dist; }
                }
            }
        }
        return minDist;
    }

//    public String toString(){
//        return Arrays.toString(new double[]{circles.getSurface()});
//    }

}

