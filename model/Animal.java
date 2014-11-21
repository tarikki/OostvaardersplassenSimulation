package model;

/**
 * Created by extradikke on 20-11-14.
 */
public class Animal implements Runnable{
    private int id;
    private int[] location = new int[2];


    public Animal(int id, int x, int y) {
        this.id = id;
        this.location[0] = x;
        this.location[1] = y;
    }

    public int getX(){
        return location[0];
    }

    public int getY(){
        return location[1];
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        this.location[0]+=10;

    }
}
