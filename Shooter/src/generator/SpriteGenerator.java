package generator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SpriteGenerator
{
    private static BufferedImage spriteImage;
    private static BufferedImage[] sprites;
    public SpriteGenerator(){}
    static BufferedImage[] generateSprite(String path, int row, int column)
    {
        if(row>0 || column >0)
        {
            sprites = new BufferedImage[row*column];
            try
            {
               spriteImage = ImageIO.read(new File(path));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            int width = spriteImage.getWidth() / column;
            int height = spriteImage.getHeight() / row;
            for(int i=0; i<row; i++)
            {
                for(int j=0; j<column; j++)
                {
                    sprites[i*column + j] = spriteImage.getSubimage(j*width, i*height, width, height);
                }

            }
        }
        return sprites;
    }
}
