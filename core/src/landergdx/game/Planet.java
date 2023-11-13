package landergdx.game;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Planet extends Circle {

    public final double mass;
    public final double G;
    public Planet(double mass, int y, int x, float rad)
    {
        this.x = x;
        this.y = y;
        this.mass = mass;
        this.radius = rad;
        G = 6.674e-11;
    }
    public double Gravity(Circle planet,Lander land)
    {
        double distance = getDistance(land);
        double grav = (G*mass*land.mass)/Math.pow(distance,2);
        return grav;
    }
    public double getDistance(Lander land)
    {
        double distance = Math.sqrt(Math.pow(x - (land.pos.x+16),2) + Math.pow(y - (land.pos.y+16),2));
        return distance;
    }

}
