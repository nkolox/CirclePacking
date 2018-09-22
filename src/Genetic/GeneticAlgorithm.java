package Genetic;

import main.Circles;
import main.Coordinate;

/**
 * Created by LocalAdm on 12.2016.
 */
public class GeneticAlgorithm {

    public static int TARGET_SIZE;
    public static Coordinate[] TARGET_COORDINATE;

    public static int getTargetSize() {
        return TARGET_SIZE;
    }

    public static int getNumOfElite() {
        return NUM_OF_ELITE;
    }

    public static int getTornamentSize() {
        return TORNAMENT_SIZE;
    }

    public static double getMutationRate() {
        return MUTATION_RATE;
    }

    public static void setMutationRate(double mutationRate) {
        MUTATION_RATE = mutationRate;
    }

    public static void setTargetSize(int targetSize) {
        TARGET_SIZE = targetSize;
    }

    public static void setTornamentSize(int tornamentSize) {
        TORNAMENT_SIZE = tornamentSize;
    }

    public static int NUM_OF_ELITE;

    public static double getELITE() {
        return ELITE;
    }

    public static void setELITE(double ELITE) {
        GeneticAlgorithm.ELITE = ELITE;
    }

    public static double ELITE = 0.2;
    public static int TORNAMENT_SIZE;
    public static double MUTATION_RATE = 0.25;
    private Circles gaCircles;
    private int r = 1;

    public Population evolve(Population pop){
        TARGET_SIZE = pop.getIndividuals().length;
        TORNAMENT_SIZE = TARGET_SIZE / 2;
        NUM_OF_ELITE = (int) Math.round(TARGET_SIZE * ELITE);
        return mutatePopulation(crossoverPopulation(pop));
    }

    private Population crossoverPopulation(Population population){
        Population crossoverPopulation = new Population(population.getIndividuals().length, false);
        for(int i = 0; i < NUM_OF_ELITE; i++){
            crossoverPopulation.getIndividuals()[i] = population.getIndividuals()[i];
        }
        for (int i = NUM_OF_ELITE; i < population.getIndividuals().length; i++){
            Individual individ1 = selectTournamentPopulation(population).getIndividuals()[0];
            Individual individ2 = selectTournamentPopulation(population).getIndividuals()[0];
            crossoverPopulation.getIndividuals()[i] = crossoverIndividuals(individ1, individ2);
        }
        return crossoverPopulation;
    }

    private Population mutatePopulation(Population population){
        Population mutatePopulation = new Population(population.getIndividuals().length, false);
        for (int i = 0; i < NUM_OF_ELITE; i++){
            mutatePopulation.getIndividuals()[i] = population.getIndividuals()[i];
        }
        for(int i = NUM_OF_ELITE; i < population.getIndividuals().length; i++){
            mutatePopulation.getIndividuals()[i] = mutateIndividual(population.getIndividuals()[i]);
        }
        return mutatePopulation;
    }

    private Individual crossoverIndividuals(Individual individ1, Individual individ2){
        Individual crossoverIndividual = new Individual(TARGET_SIZE);
        for(int i = 0; i < individ1.getGenes().length; i++){
            if(Math.random() < MUTATION_RATE)
            {
                crossoverIndividual.setCircles(individ1.getCircles());
                crossoverIndividual.getGenes()[i] = individ1.getGenes()[i];
            }
            else {
                crossoverIndividual.setCircles(individ2.getCircles());
                crossoverIndividual.getGenes()[i] = individ2.getGenes()[i];
            }
        }
        return crossoverIndividual;
    }

    private Individual mutateIndividual(Individual individual){
        Individual mutateIndividual = new Individual(TARGET_SIZE);
        Circles circles = new Circles(TARGET_SIZE, r);
        double x, y;
        int wp = 1000;

        for(int i = 0; i < individual.getGenes().length; i++){
            if(Math.random() < MUTATION_RATE){
                x=wp*Math.random();
                y=wp*Math.random();
                mutateIndividual.setCircles(circles);
                mutateIndividual.getGenes()[i] = new Coordinate(x, y);
            }
            else {
                mutateIndividual.setCircles(circles);
                mutateIndividual.getGenes()[i] = individual.getGenes()[i];
            }
        }
        return mutateIndividual;
    }

    private Population selectTournamentPopulation(Population population){
        Population tournamentPopulation = new Population(TORNAMENT_SIZE, false);

        for(int i = 0; i < TORNAMENT_SIZE; i++){
            tournamentPopulation.getIndividuals()[i] =
                    population.getIndividuals()[(int)Math.random()*population.getIndividuals().length];
        }
        tournamentPopulation.sortIndividualsByFitness(); // works right
        return tournamentPopulation;
    }

}
