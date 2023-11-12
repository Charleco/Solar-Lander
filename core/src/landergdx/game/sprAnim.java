package landergdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class sprAnim {
    private TextureRegion[][] spritesheet;
    private TextureRegion[] splitsheet;
    public sprAnim()
    {

    }
    public Animation<TextureRegion> getAnim(int rowCount, int colCount, Texture sheet, float fps)
    {
        Animation<TextureRegion> output;
        spritesheet = TextureRegion.split(sheet, sheet.getWidth() / colCount, sheet.getHeight() / rowCount);
        splitsheet = new TextureRegion[colCount*rowCount];
        int index = 0;
        for(int i = 0; i<rowCount;i++)
        {
            for(int j=0; j<colCount;j++)
            {
                splitsheet[index++] = spritesheet[i][j];
            }
        }
        output = new Animation<TextureRegion>(fps, splitsheet);
        return output;
    }
}
