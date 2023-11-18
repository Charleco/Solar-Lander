package landergdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

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
    public void debugUpdate(Label label) {
        label.setText("Debug: "+"\n"+"Memory Usage: "+this.getMemUsage()/(1024)+"kB"+"\n"+"Window Size: "+ Gdx.graphics.getHeight()+"x"+Gdx.graphics.getWidth()+"\n" + "DeltaTime: "+Gdx.graphics.getDeltaTime());
    }
    public void velLabelUpdate(Label label,Lander land) {
        label.setText("Lander: "+"\n"+"Velx: "+land.vel.x+"\n"+"Vely: "+land.vel.y+"\n"+"X: "+land.pos.x+"\n"+"Y: "+land.pos.y);
    }
    public void obLabelUpdate(Label label,solarObject ob, int i) {
        label.setText("Planet " +(i+1)+":"+ "\n"+"Velx: "+ob.vel.x+"\n"+"Vely: "+ob.vel.y+"\n"+"X: "+ob.pos.x+"\n"+"Y: "+ob.pos.y+"\n"+"Mass: "+ob.mass);
    }
}
