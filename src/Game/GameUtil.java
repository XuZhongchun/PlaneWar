package Game;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class GameUtil {
    public static Image getImage(String filepath){
        BufferedImage img=null;
        try{
            URL url=GameUtil.class.getClassLoader().getResource(filepath);
            img=ImageIO.read(url);
        }catch(IOException e){
            e.printStackTrace();
        }
        return img;
    }

    public static void main(String[] args) {
        System.out.println(getImage("image/bg.jpg"));
    }
}
