package Genetic;

import main.Circles;

import java.util.Arrays;

/**
 * Created by LocalAdm on 24.12.2016.
 */
public class Population {

    Individual[] individuals;

    Circles circles;
    int wp = 1000;

    public Population(int length)
    {
        individuals = new Individual[length];
    }

    public Individual[] getIndividuals(){
        return individuals;
    }

    public void sortIndividualsByFitness(){
        Arrays.sort(individuals, (individual1, individual2) -> {
            int flag = 0;
            if (individual1.getFitness() > individual2.getFitness()) flag = -1;
            else if(individual1.getFitness() < individual2.getFitness()) flag = 1;
            return flag;
        });
    }

    // Create a population
    public Population(int populationSize, boolean initialise) {
        individuals = new Individual[populationSize];

        // Initialise population
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < size(); i++) {
                Individual newIndividual = new Individual(populationSize);
                newIndividual.initializeIndividual();
                saveIndividual(i, newIndividual);
                individuals[i] = newIndividual;
            }
            sortIndividualsByFitness();
        }

    }

    // Sort individuals by fitness
    public Individual getFittest() {
        Individual fittest = individuals[0];
        // Loop through individuals to find fittest
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() <= getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    /* Public methods */
    // Get population size
    public int size() {
        return individuals.length;
    }

    /* Getters */
    public Individual getIndividual(int index) {
        return individuals[index];
    }

    // Save individual
    public void saveIndividual(int index, Individual indiv) {
        individuals[index] = indiv;
    }

}
