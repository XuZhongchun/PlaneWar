package Game;

import java.awt.*;

public class Explode {
    double x,y;

    static Image[]explode=new Image[4];
    static Image[]bossExplode=new Image[4];
    static {
        for(int i=0;i<explode.length;++i)
            explode[i]=GameUtil.getImage("image/explode"+(i+1)+".png");
        for(int i=0;i<bossExplode.length;++i)
            bossExplode[i]=GameUtil.getImage("image/bossExplode"+(i+1)+".png");
    }
    public Explode(){}
    public Explode(double x,double y){
        this.x=x;
        this.y=y;
    }

    public void draw(Graphics g){
        for(int i=0;i<explode.length;++i)
            g.drawImage(explode[i],(int)x,(int)y,null);
    }

    public void drawBossExplode(Graphics g){
        for (int i = 0; i < bossExplode.length; ++i)
            g.drawImage(bossExplode[i], (int) x, (int) y, null);
    }
}
