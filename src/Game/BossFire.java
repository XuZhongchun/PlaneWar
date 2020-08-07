package Game;

import java.awt.*;

public class BossFire extends GameObject{
    double degree;
    boolean live;

    public BossFire(){}
    public BossFire(double x,double y,int speed,double degree){
        img=GameUtil.getImage("image/bossfire.png");
        this.x=x;
        this.y=y;
        width=10;
        height=10;
        this.speed=speed;
        this.degree=degree;
        live=true;
    }

    public boolean isLive(){
        return live&&!isOut();
    }

    public boolean isOut(){
        return outside()!=0;
    }

    //重写GameObject的方法
    public void draw(Graphics g) {
        super.draw(g);
        x+=speed*Math.cos(degree*Math.PI/180);//1°=π/180rad
        y+=speed*Math.sin(degree*Math.PI/180);
    }
}
