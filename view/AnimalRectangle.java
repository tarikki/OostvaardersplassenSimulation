package view;

import model.Animal;

import java.awt.*;

/**
 * Created by Pepe on 27.11.2014.
 */
public class AnimalRectangle extends Rectangle {
    private String name;
    protected String report;
    private Animal animal;


    public String getName() {
        return name;
    }

    public AnimalRectangle(int x, int y, int width, int height, Animal animal, String name) {

        super(x, y, width, height);
        this.animal = animal;

        this.report = this.animal.report();
        this.name = name;


    }


}

