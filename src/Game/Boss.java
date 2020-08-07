package Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Boss extends GameObject{
    int hp,moveDelay,attackDelay,mlr;//0中间，1左边，2右边
    Random random;
    ArrayList<BossFire>bossfire;
    int bloodGridSum;//计算血量位置的参数，使效果更美观

    public Boss(){
        img=GameUtil.getImage("image/boss.png");
        x=GameFrame.WIDTH/2-10;
        y=20;
        width=img.getWidth(null);
        height=img.getHeight(null);
        speed=10;
        hp=50;
        random=new Random();
        moveDelay=random.nextInt(20);
        mlr=random.nextInt(3);
        bossfire=new ArrayList<BossFire>();
        bloodGridSum=hp;
    }

    public void drawSelf(Graphics g) {
        draw(g);
        if(outside()==1)//碰到左墙往右走
            mlr=2;
        else if(outside()==2)//碰到右墙往左走
            mlr=1;
        else if(moveDelay<=0) {
            mlr=random.nextInt(3);
            moveDelay=random.nextInt(20);
        }
        if(mlr==1)x-=speed;
        else if(mlr==2)x+=speed;
        --moveDelay;
    }

    public void drawBlood(Graphics g){
        Color color=g.getColor();
        g.setColor(Color.red);
        g.fillRect(10,10,hp*(GameFrame.WIDTH-35)/bloodGridSum,10);
        g.setColor(color);
    }

    public void drawFire(Graphics g){
        for(int i=0;i<bossfire.size();++i){
            BossFire bf=bossfire.get(i);
            if(bf.isLive())bf.draw(g);
            else{
                if(!bf.isOut())
                    new Explode(bf.x,bf.y).draw(g);
                bossfire.remove(i);
            }
        }
    }

    public boolean isLive(){
        return hp>0;
    }

    public void injure(int power){
        hp-=power;
    }

    public void over(Graphics g){
        bossfire.clear();
        new Explode(x,y).drawBossExplode(g);
    }

    public void attack(){
        if(attackDelay>=50&&isLive()) {
            attackDelay = 0;
            double x = this.x + width / 2;
            double y = this.y + height;
            int speed;
            if (random.nextInt(2) == 0) {
                speed = 10;
                for (int i = 0; i < 3; ++i) {
                    bossfire.add(new BossFire(x, y, speed, 60));//speed=10：5发3层弹幕，speed=20：3发3层弹幕
                    bossfire.add(new BossFire(x, y, speed, 75));
                    bossfire.add(new BossFire(x, y, speed, 90));
                    bossfire.add(new BossFire(x, y, speed, 105));
                    bossfire.add(new BossFire(x, y, speed, 120));
                    speed *= 0.8;
                }
            } else {
                speed = 15;
                for (int i = 0; i < 3; ++i) {
                    bossfire.add(new BossFire(x, y, speed, 80));
                    bossfire.add(new BossFire(x, y, speed, 90));
                    bossfire.add(new BossFire(x, y, speed, 100));
                    speed *= 0.8;
                }
            }
        }
        ++attackDelay;
    }

    //重载GameObject的方法
    public boolean hitted(GameObject attacked) {
        for(int i=0;i<bossfire.size();++i) {
            BossFire bf=bossfire.get(i);
            if(bf.isLive()&&hitted(bf,attacked)) {
                bf.live=false;
                return true;
            }
        }
        return false;
    }
}
