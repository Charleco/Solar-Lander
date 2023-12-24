package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;

public class userInterface {
    public userInterface()
    {
    }
    public long getMemUsage()
    {
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        return heapSize - heapFreeSize;
    }
    public void debugUpdate(Label label, float stateTime) {
        label.setText("Debug: "+"\n"+"Memory Usage: "+this.getMemUsage()/(1024)+"kB"+"\n"+"Window Size: "+ Gdx.graphics.getHeight()+"x"+Gdx.graphics.getWidth()+"\n" + "DeltaTime(ms): "+Gdx.graphics.getDeltaTime()*1000+"\n"+"Total Time(s): "+Math.round(stateTime));
    }
    public void velLabelUpdate(Label label,Lander land) {
        label.setText("Lander: "+"\n"+"Velx: "+ land.getVel().x+"\n"+"Vely: "+ land.getVel().y+"\n"+"X: "+ land.getPos().x+"\n"+"Y: "+ land.getPos().y);
    }
    public void obLabelUpdate(ArrayList<Label> labels, solarObject[] system) {
        for(int i =0;i<system.length;i++)
        {
            solarObject ob = system[i];
            labels.get(i).setText("Planet " + (i + 1) + ":" + "\n" + "Velx: " + ob.getVel().x + "\n" + "Vely: " + ob.getVel().y + "\n" + "X: " + ob.getPos().x + "\n" + "Y: " + ob.getPos().y + "\n" + "Mass: " + ob.getMass());
        }
    }
    public void camZoom(OrthographicCamera cam)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
            cam.zoom = 10f;
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.X))
        {
            cam.zoom = 100f;
        }
        else{
            cam.zoom = 1f;
        }
    }
}
