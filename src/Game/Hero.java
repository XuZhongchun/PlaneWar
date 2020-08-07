package Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Hero extends GameObject{
    boolean left,right,up,down,isAttack;
    int hp,power;
    Explode explode;
    ArrayList<HeroFire>herofire;
    int bloodGridSum;

    public Hero(){
        img=GameUtil.getImage("image/hero.png");
        x=250;
        y=400;
        width=50;
        height=50;
        speed=7;
        hp=3;
        power=1;
        herofire=new ArrayList<HeroFire>();
        bloodGridSum=hp+1;
    }

    public void drawSelf(Graphics g) {
        draw(g);
        if(left&&outside()!=1)
            x-=speed;
        if(right&&outside()!=2)
            x+=speed;
        if(up&&outside()!=3)
            y-=speed;
        if(down&&outside()!=4)
            y+=speed;
    }

    public void drawBlood(Graphics g){
        for(int i=0;i<hp;++i)
            g.drawImage(img,GameFrame.WIDTH-bloodGridSum*30+i*30,GameFrame.HEIGHT-80,30,30,null);
    }

    public void drawFire(Graphics g){
        for(int i=0;i<herofire.size();++i)
            herofire.get(i).draw(g);
        if(explode!=null){
            explode.draw(g);
            explode=null;
        }
    }

    public void attack(){
        if(isAttack)
            herofire.add(new HeroFire(x+width*0.35,y-height/2));
    }

    public boolean isLive(){
        return hp>0;
    }

    public void injure(int power){
        hp-=power;
    }

    public void act(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                up=true;
                break;
            case KeyEvent.VK_DOWN:
                down=true;
                break;
            case KeyEvent.VK_LEFT:
                left=true;
                break;
            case KeyEvent.VK_RIGHT:
                right=true;
                break;
            case KeyEvent.VK_SPACE:
                isAttack=true;
                break;
        }
    }

    public void stop(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                up=false;
                break;
            case KeyEvent.VK_DOWN:
                down=false;
                break;
            case KeyEvent.VK_LEFT:
                left=false;
                break;
            case KeyEvent.VK_RIGHT:
                right=false;
                break;
            case KeyEvent.VK_SPACE:
                isAttack=false;
                break;
        }
    }

    //重载GameObject的方法
    public boolean hitted(Boss boss) {
        for (int i = 0; i < herofire.size(); ++i) {
            HeroFire hf=herofire.get(i);
            if (boss.isLive()&&hitted(hf,boss)) {
                explode=new Explode(hf.x,hf.y);
                herofire.remove(i);
                return true;
            }
        }
        return false;
    }
    public int hitted(ArrayList<Enemy>enemy){
        int res=0;
        for (int i=0;i<herofire.size();++i) {
            boolean flag=false;
            HeroFire bf=herofire.get(i);
            for(int j=0;j<enemy.size();++j) {
                Enemy e=enemy.get(j);
                if (e.isLive()&&hitted(bf,e)) {
                    e.injure();
                    flag=true;
                    ++res;
                }
            }
            if(flag)herofire.remove(i);
        }
        return res;
    }

    public Rectangle getRec(){
        double x=this.x+this.width/4;
        double y=this.y+this.height/5;
        int width=this.width/2;
        int height=this.height*4/5;
        return new Rectangle((int)x,(int)y,width,height);
    }
}