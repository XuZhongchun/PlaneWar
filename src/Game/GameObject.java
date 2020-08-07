package Game;

import java.awt.*;

public class GameObject{
    Image img;
    double x,y;
    int width,height,speed;

    public GameObject(){}
    public GameObject(Image img, double x, double y, int width, int height, int speed) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
    }

    public void draw(Graphics g) {
        g.drawImage(img,(int)x,(int)y,width,height,null);
    }

    public Rectangle getRec(){
        return new Rectangle((int)x,(int)y,width,height);
    }

    public boolean hitted(GameObject attacker,GameObject attacked){
        return attacker.getRec().intersects(attacked.getRec());
//		boolean flag=attacker.x+attacker.width<attacked.x||
//				     attacker.y+attacker.height<attacked.y||
//				     attacker.x>attacked.x+attacked.width||
//				     attacker.y>attacked.y+attacked.height;
//		return !flag;
    }

    public int outside(){
        if(x<0)return 1;
        if(x+width>GameFrame.WIDTH)return 2;
        if(y<0)return 3;
        if(y+height*1.5>GameFrame.HEIGHT)return 4;
        return 0;
    }
}