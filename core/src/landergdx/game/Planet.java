package landergdx.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Planet extends Circle {

    private final double mass;


    public Planet(double mass, int y, int x, float rad)
    {

        this.x = x;
        this.y = y;
        this.mass = mass;
        this.radius = rad;
    }
    public double Gravity(Circle planet,Lander land)
    {
        double G = 6.674e-11;
        double distance = Math.sqrt(Math.pow(planet.x - land.pos.x,2) + Math.pow(planet.y - land.pos.y,2));
        double grav = (G*mass*land.mass)/Math.pow(distance,2);
        return grav;
    }

}
