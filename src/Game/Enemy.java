package Game;

import java.awt.*;
import java.util.Random;

public class Enemy extends GameObject{
    int mlr;
    boolean live;
    Random random;

    public Enemy(){
        img=GameUtil.getImage("image/enemy.png");
        random=new Random();
        x=random.nextInt(GameFrame.WIDTH-50);
        y=0;
        width=50;
        height=50;
        speed=random.nextInt(10)+5;
        mlr=random.nextInt(3);
        live=true;
    }

    public boolean isLive(){
        return live&&!isOut();
    }

    public boolean isOut(){
        return outside()==4;
    }

    public void injure(){
        live=false;
    }

    //重写GameObject的方法
    public void draw(Graphics g) {
        super.draw(g);
        if(outside()==1)
            mlr=2;
        else if(outside()==2)
            mlr=1;
        if(mlr==1)x-=speed;
        else if(mlr==2)x+=speed;
        y+=speed;
    }

    //重载GameObject的方法
    public boolean hitted(GameObject attacked) {
        return hitted(this, attacked);
    }
}
