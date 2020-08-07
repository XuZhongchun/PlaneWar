package Game;

import java.awt.*;

public class HeroFire extends GameObject{
    public HeroFire(){}
    public HeroFire(double x, double y){
        img=GameUtil.getImage("image/herofire.png");
        this.x=x;
        this.y=y;
        width=img.getWidth(null);
        height=img.getHeight(null);
        speed=100;
    }
    public HeroFire(Image img, double x, double y, int speed){
        this.img=img;
        this.x=x;
        this.y=y;
        width=img.getWidth(null);
        height=img.getHeight(null);
        this.speed=speed;
    }

    //重写GameObject的方法
    public void draw(Graphics g) {
        super.draw(g);
        y-=speed;
    }
}
