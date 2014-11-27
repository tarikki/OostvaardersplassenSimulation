package view;

import model.Animal;

import java.awt.*;

/**
 * Created by Pepe on 27.11.2014.
 */
public class AnimalRectangle extends Rectangle {
    private int id;
    protected String report;
    private Animal animal;


    public AnimalRectangle(int x, int y, int width, int height, Animal animal) {

        super(x, y, width, height);
        this.animal = animal;
        this.report = this.animal.report();


    }


}

