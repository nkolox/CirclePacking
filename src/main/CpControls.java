package main;
import java.awt.*;

/**
 * Created by LocalAdm on 23.12.2016.
 */
public class CpControls extends Panel {
        TextField circlesN = new TextField(4);
        TextField circlesTr = new TextField(8);
        TextField eliteNum = new TextField(2);
        TextField mutateP = new TextField(2);
        TextField endGA = new TextField(2);
        TextField stepSize = new TextField(3);
        //Checkbox view = new Checkbox(" Show me only the best results");

        public CpControls() {
            add(new Label("Number of circles:"));
            add(circlesN);
            add(new Label("Step:"));
            add(stepSize);
            add(new Label("Number of try-outs:"));
            add(circlesTr);
            add(new Label("Part of Elite:"));
            add(eliteNum);
            add(new Label("Mutation probability:"));
            add(mutateP);
            add(new Label("Target Density:"));
            add(endGA);

            add(new Button(" Start NN "));
            add(new Button(" Start GA "));
            add(new Button(" Stop "));
            add(new Button(" Clear "));
            //add(view);
            setBackground(Color.white);
        }
    }
