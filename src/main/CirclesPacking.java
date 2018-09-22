package main;

/*<APPLET CODE="CirclesPacking.class" width=720 height=420>
<param name=n0 value=0.0>
<param name=n1 value=0.785398163397>
<param name=n2 value=0.539012084453>
...
<param name=n200 value=0.840434362111>
</APPLET>
*/

import Genetic.GeneticAlgorithm;
import Genetic.Population;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

// Class definition CirclesPacking

public class CirclesPacking extends Applet implements Runnable {
    Graphics g;

    //Genetic for init
    GeneticAlgorithm evolution;
    // a korok kozeppontjat tarolja a "virtualis" negyzetben
    Coordinate origos[];
    // Ages to plot the centers of storage
    Coordinate realOrigos[];
    // the best centers of storage
    Coordinate best[];
    // Storage properties of the ages
    Circles circles;
    // The user-interface elements stores
    CpControls controls;
    Thread CpThread = null;

    public void setStep(double step) {
        this.step = step;
        controls.stepSize.setText("" + getStep());
    }

    public double getStep() {
        return step;
    }

    // Move to the initial level
    double step;
    // Move the magnitude of this share
    static final double div=1.5;
    // Move up the value of these can be small
    static final double limit=0.01;
    static final int wp=1000;
    // The ages actual field of storage
    double s;

    // Retry the current storage
    int t;
    // This string is the HTML species taken over appropriate parameters
    String bests;
    // True if the check box is turned
    boolean view;
    // True if the NN algorithm runs
    boolean doing;
    // True if the Genetic algorithm runs
    boolean ga;

    double targetSurface;
    double rad;

    // Number og generation for GA
    int generationNumber;
    double gaDensity;
    private static final int L = 350;
    double targetDensity = 0.75;
    Timer timer;

    double k = 0;
// Initialization

    public void init() {
        g=getGraphics();
        setLayout(new BorderLayout());
        controls=new CpControls();
        add("South", controls);
        setCircles(new Circles(9, 100));
        setStep(200);
        bests="0.0";
        setEvolution(new GeneticAlgorithm());
        s=0;
        t=0;
        rad = 0;
        gaDensity = 0;
        generationNumber = 0;
        view=true;
        doing = false;
        ga = false;
        repaint();
    }

// a Start function

    public void start() {
        CpThread = new Thread(this);
        CpThread.start();
    }

// Called the Start

    public void run() {
        while(true) {
            if(doing == true) {
                examine();
                doing = false;
            }
            else if(ga == true && doing == false)
            {
                examineGA();
                ga = false;
            }
            try {
                Thread.sleep(100);
            }
            catch(InterruptedException e) {
            }
        }
    }

    public void setCircles(Circles circles) {

        this.circles=circles;
        controls.circlesN.setText("" + circles.getN());
        controls.circlesTr.setText("" + circles.getTr());
        targetSurface = circles.getN()*Math.PI*(1/(L-2))*(1/(L-2));
    }

    public void setEvolution(GeneticAlgorithm evolution){
        this.evolution = evolution;
        controls.eliteNum.setText("" + evolution.getELITE());
        controls.mutateP.setText("" + evolution.getMutationRate());
        controls.endGA.setText("" + targetDensity);
    }

    public void examine() {
        double r = 0;

        origos = new Coordinate[circles.n];
        realOrigos = new Coordinate[circles.n];
        best = new Coordinate[circles.n];
        if(circles.n == 0) {
            s=0;
            repaint();
        }
        if(circles.n == 1) {
            circles.r=0.5;
            s=circles.getSurface();
            repaint();
        }
        else if(circles.n>1) {
            for(int i=1; i<=circles.tr; ++i) {
                if(doing==false) {
                    break;
                }
                t=i;
                movePoints();
                if(circles.r>r) {
                    r=circles.r;
                    s=circles.getSurface();
                    circlesDraw(best, 0);
                }
            }
        }
    }

    public void examineGA(){
        double r = 0;

        origos = new Coordinate[circles.n];
        realOrigos = new Coordinate[circles.n];
        best = new Coordinate[circles.n];
        if(circles.n == 0) {
            s=0;
//            gaDensity=0;
            repaint();
        }
        if(circles.n == 1) {
            circles.r=0.5;
            s=circles.getSurface();
//            gaDensity = circles.getSurface();
            repaint();
        }
        else if(circles.n>1) {
            for(int i=1; i<=circles.tr; ++i) {
                if(ga==false) {
                    break;
                }
                t=i;
                movePointsGA();
                if(circles.r > r) {
                    s=circles.getSurface();
//                    gaDensity = circles.getSurface();
                    circlesDraw(best, 0);
                }
            }
        }
    }

// Randomly place the items in the "virtual" box

    public void randomPoints() {
        double x,y;

        for(int i=0; i<=(circles.n-1); ++i) {
            x=wp*Math.random();
            y=wp*Math.random();
            origos[i] = new Coordinate(x, y);
        }
    }

// Points of movement responsible All Started

    public void movePoints() {
        boolean moved;
        double closest;
        double distance;
        double x, y;

        step=200;
        randomPoints();
        circles.r=(minimumDistance(origos)/2.0);
        if(circles.r==0.0) {
            do {
                randomPoints();
                circles.r=(minimumDistance(origos)/2.0);
            } while(circles.r==0.0);
        }
        do {
            if(doing==false) {
                break;
            }
            moved=false;
            for(int i=0; i<=(circles.n-1); ++i) {
                x=origos[i].x;
                y=origos[i].y;
                closest=nearestNeighbour(origos, i, x, y);
                x=origos[i].x+step;
                if(x>wp) { x=wp; }
                distance=nearestNeighbour(origos, i, x, y);
                if(distance>closest) {
                    origos[i].x=x;
                    moved=true;
                    closest=nearestNeighbour(origos, i, x, y);
                }
                x=origos[i].x-step;
                y=origos[i].y;
                if(x<0) { x=0; }
                distance=nearestNeighbour(origos, i, x, y);
                if(distance>closest) {
                    origos[i].x=x;
                    moved=true;
                    closest=nearestNeighbour(origos, i, x, y);
                }
                y=origos[i].y+step;
                x=origos[i].x;
                if(y>wp) { y=wp; }
                distance=nearestNeighbour(origos, i, x, y);
                if(distance>closest) {
                    origos[i].y=y;
                    moved=true;
                    closest=nearestNeighbour(origos, i, x, y);
                }
                y=origos[i].y-step;
                x=origos[i].x;
                if(y<0) { y=0; }
                distance=nearestNeighbour(origos, i, x, y);
                if(distance>closest) {
                    origos[i].y=y;
                    moved=true;
                }
            }
            if(moved==false ) { step/=div; }
            circles.r=(minimumDistance(origos)/2.0);
            circlesDraw(realOrigos, 370);
        } while(step>limit);
    }

    //MOVE FOR GA
    public void movePointsGA(){
        boolean moved;

        step=200;
        randomPoints();
        circles.r = (minimumDistance(origos)/2.0);
        if(circles.r == 0.0) {
            do {
                randomPoints();
                circles.r = (minimumDistance(origos)/2.0);
            } while(circles.r == 0.0);
        }
        do {
            if(ga == false) {
                break;
            }
            // TODO
            moved=false;
            Population population = new Population(circles.n, true);
            GeneticAlgorithm geneticAlgorithm = evolution;/*new GeneticAlgorithm();*/
            origos = population.getIndividuals()[0].getGenes();

            generationNumber = 0;
            // several generations
            while(ga/*population.getIndividuals()[0].getFitness() < targetDensity*/)
            {
                generationNumber++;
                population = geneticAlgorithm.evolve(population);
                population.sortIndividualsByFitness();
                gaDensity = population.getIndividuals()[0].getFitness();
                origos = population.getIndividuals()[0].getGenes();
                if(gaDensity > targetDensity) {
                    this.g.drawString("Density: " + gaDensity, 470, 430);
                    this.g.clearRect(540, 430, 100, 40);
                }
                moved = true;
//                targetDensity += 0.01;
            }


//            if(moved==false ) { step /= div; } //COMMENTED RIGHT
            circles.r=(minimumDistance(origos)/2.0);
            rad = circles.r;
            circlesDraw(realOrigos, 370);
        } while(step > limit);
    }

// Returns the point with closest point distance

    public double nearestNeighbour(Coordinate points[], int index, double a, double b) {
        double nearest;
        double dist;

        nearest=Math.sqrt(2*wp*wp);
        for(int j=0; j<=(circles.n-1); ++j) {
            if(index!=j) {
                dist=Math.sqrt(((a-points[j].x)*(a-points[j].x))
                        +((b-points[j].y)*(b-points[j].y)));
                if(dist<nearest)  { nearest=dist; }
            }
        }
        return nearest;
    }

// Returns the smallest distance between himself points

    public double minimumDistance(Coordinate points[]) {
        double minDist;
        double dist;

        minDist=Math.sqrt(((points[0].x-points[1].x)*(points[0].x-points[1].x))
                +((points[0].y-points[1].y)*(points[0].y-points[1].y)));
        for(int i=0; i<=(circles.n-1); ++i) {
            for(int j=0; j<=(circles.n-1); ++j) {
                if(i != j) {
                    dist=Math.sqrt(((points[i].x-points[j].x)*(points[i].x-points[j].x))
                            +((points[i].y-points[j].y)*(points[i].y-points[j].y)));
                    if(dist<minDist)  { minDist=dist; }
                }
            }
        }
        return minDist;
    }

/* origos points to transform the realOrigos points
that is, a "virtual" square box from the real places in the points
*/

    public void transform(Coordinate points[]) {
        double rate;
        double x,y;

        rate=350/(wp+2*circles.r);
        for(int i=0; i<=(circles.n-1); ++i) {
            x=(rate*origos[i].x);
            y=(rate*origos[i].y);
            points[i]=new Coordinate(x,y);
        }
    }

// rendering is performed through the ages

    public void circlesDraw (Coordinate points[], int shift) {
        int x,y;
        int d;

        setBackground(Color.white);
        if(shift==0 || (shift==370 && view==true)) {
            transform(points);
            g.setColor(Color.white);
            g.fillRect(0+shift-2, 0, 354, 354);
            g.setColor(Color.black);
            g.drawRect(0+shift, 0, 350, 350);
            d=(int) (minimumDistance(points));
            for(int i=0; i<=(circles.n-1); ++i) {
                x=(int) (points[i].x+d/2);
                y=(int) (points[i].y+d/2);
                g.setColor(Color.gray);
                g.setColor(Color.black);
                g.drawOval((x-d/2)+shift, y-d/2, d, d);
                g.fillOval(x-2+shift, y-2, 3, 3);
            }
        }
        else {
            g.setColor(Color.white);
            g.fillRect(370, 0, 352, 352);
            g.setColor(Color.black);
            g.drawRect(370, 0, 350, 350);
        }

        g.setColor(Color.white);
        g.fillRect(470,351,251,70);
        g.setColor(Color.black);
        g.drawString("Number of the present try-out: " + t, 470, 370);
//        g.drawString("Density: " + gaDensity, 470, 430);
        g.drawString("Radius is: " + rad, 470, 460);
        g.drawString("Population number is: " + generationNumber, 470, 400);

        if(shift==0) {
            g.setColor(Color.white);
            g.fillRect(0,351,469,70);
            g.setColor(Color.black);
            g.drawString("Density: "+ s, 10, 370);

            g.setColor(Color.red);
            if(circles.n<=200) {
                if(t==1) {
                    bests=getParameter("n"+circles.n);
                }
//                g.drawString("The best density known yet: " + bests,190, 370);
            }
            else {
//                g.drawString("The best density known yet: unknown",190, 370);
            }
        }
    }

// Paints the box

    public void paint(Graphics g) {
        int x,y;
        int d;

        setBackground(Color.white);
        g.setColor(Color.black);
        g.drawRect(0, 0, 350, 350);
        g.drawRect(370, 0, 350, 350);
        g.drawString("Density: "+s, 10, 370);
//        g.drawString("Density: " + gaDensity, 470, 430);
        g.drawString("Radius is: " + rad, 470, 460);

        g.drawString("Population number is: " + generationNumber, 470, 400);
        if(circles.n==0) {
            bests=getParameter("n"+circles.n);
        }
        if(circles.n==1) {
            bests=getParameter("n"+circles.n);
        }
        g.setColor(Color.red);
//        g.drawString("The best density known yet: " + k/*bests*/, 190, 370);
        g.drawString("Number of the present try-out: " + t, 470, 370);

        if(circles.n==1) {
            g.setColor(Color.gray);
            g.fillOval(0, 0, 350, 350);
            g.setColor(Color.black);
            g.drawOval(0, 0, 350, 350);
            g.fillOval(173, 173, 4, 4);
            if(view==true) {
                g.setColor(Color.gray);
                g.fillOval(370, 0, 350, 350);
                g.setColor(Color.black);
                g.drawOval(370, 0, 350, 350);
                g.fillOval(543, 173, 4, 4);
            }
        }
        if(t>=1 && (circles.n>1)) {
            d=(int) (minimumDistance(best));
            for(int i=0; i<=(circles.n-1); ++i) {
                x=(int) (best[i].x+d/2);
                y=(int) (best[i].y+d/2);
                g.setColor(Color.gray);
                g.fillOval(x-d/2, y-d/2, d, d);
                g.setColor(Color.black);
                g.drawOval(x-d/2, y-d/2, d, d);
                g.fillOval(x-2, y-2, 4, 4);
            }
        }
        if((t==circles.tr && view==true) && (circles.n>1)) {
            d=(int) (minimumDistance(realOrigos));
            for(int i=0; i<=(circles.n-1); ++i) {
                x=(int) (realOrigos[i].x+d/2);
                y=(int) (realOrigos[i].y+d/2);
                g.setColor(Color.gray);
                g.fillOval((x-d/2)+370, y-d/2, d, d);
                g.setColor(Color.black);
                g.drawOval((x-d/2)+370, y-d/2, d, d);
                g.fillOval((x-2)+370, y-2, 4, 4);
            }
        }
    }

// Handles the events

    public boolean action(Event e, Object arg) {
        Checkbox id;

        if(e.target instanceof Button) {
            String label=(String) arg;
            if (label.equals(" Start NN ")) {
                circles.setN( Integer.parseInt(controls.circlesN.getText()));
                circles.setTr( Integer.parseInt(controls.circlesTr.getText()));
                this.step = Double.parseDouble(controls.stepSize.getText());
                setCircles(circles);
                doing = true;
                ga = false;

            }
            else if(label.equals(" Start GA ")) {
                circles.setN( Integer.parseInt(controls.circlesN.getText()));
                circles.setTr( Integer.parseInt(controls.circlesTr.getText()));
                setCircles(circles);
                evolution.setMutationRate(Double.parseDouble(controls.mutateP.getText()));
                evolution.setELITE(Double.parseDouble(controls.eliteNum.getText()));
                targetDensity = Double.parseDouble(controls.endGA.getText());
                ga = true;
                doing = false;

            }
            else if(label.equals(" Clear ")) {
                init();
            }
            else{
                doing = false;
                ga = false;
            }
        }
        if(e.target instanceof Checkbox) {
            id = (Checkbox) e.target;
            if(id.getLabel() == " Show me only the best results") {
                if(id.getState()) {
                    view=false;
                }
                else {
                    view=true;
                }
            }
        }

        return true;
    }

}