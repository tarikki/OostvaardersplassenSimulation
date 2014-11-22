package model;

/**
 * Created by extradikke on 20-11-14.
 */
public class Animal implements Runnable{
    private int id;
    private int sleepiness = 0;
    private int hunger = 0;
    private int thirst = 0;
    private int energy;
    private boolean living = true;
    private boolean tired = false;

    private int[] location = new int[2];
    private int[] destination = new int[2];


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

    public void eat()
    {
        this.energy += 1;

    }

    public void drink()
    {

    }

    public void move()
    {

    }

    public void sleep()
    {

    }



    public void report()
    {
        System.out.println("Animal [id = " + this.id + ", Energy = " + this.energy + ", Living = " + this.living + "]");
    }

    @Override
    public void run() {
        this.location[0]+=10;

    }
}
