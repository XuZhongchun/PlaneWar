package Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GamePanel extends JPanel {
	public static final int CLOSED=0,RUNNING=1,CLOSING=2,GAMEOVER=3,WIN=4;
	int state;//状态
	int score;//分数
	int cnt;//生成敌人的间隔
	int sum;//敌人数量
	int spendTime;//击败boss花费的时间
	long appearTime;//boss出现的时间
	boolean bossAppeared;
	GameFrame gf;
	GameObject bg;
	Hero hero;
	Boss boss;
	ArrayList<Enemy>enemy=new ArrayList<Enemy>();
	Image img=GameUtil.getImage("image/bg.jpg");

	public GamePanel(){}
	public GamePanel(GameFrame gf){
		state=RUNNING;
		sum=20;
		bg=new GameObject(img,0,0,GameFrame.WIDTH,GameFrame.HEIGHT,0);
		hero=new Hero();
		new Thread(new UpdateThread(this)).start();
		this.gf=gf;
		gf.addKeyListener(new KeyMonitor());
	}

	@Override
	public void paintComponent(Graphics g) {
		bg.draw(g);
		for(int i=0;i<enemy.size();++i) {
			Enemy e=enemy.get(i);
			if(e.isLive())e.draw(g);
			else {
				if(!e.isOut())new Explode(e.x,e.y).draw(g);
				enemy.remove(i);
			}
		}
		if(bossAppeared) {
			if(boss.isLive()) {
				boss.drawSelf(g);
				boss.drawFire(g);
				boss.drawBlood(g);
			}
			else if(state==CLOSING){
				boss.over(g);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(500);
							state = CLOSED;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
				bossAppeared = false;
			}
		}
		if(hero.isLive()) {
			hero.drawSelf(g);
			hero.drawFire(g);
			hero.drawBlood(g);
		}
		else if(state==CLOSING){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);
						state = CLOSED;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		Color color=g.getColor();
		g.setColor(Color.white);
		g.setFont(new Font("楷体",Font.BOLD,20));
		g.drawString("分数："+score,10,GameFrame.HEIGHT-60);
		g.setColor(color);
	}

	private Image offScreenImage;
	@Override
	public void update(Graphics g) {
		if(offScreenImage==null)
			offScreenImage=createImage(GameFrame.WIDTH,GameFrame.HEIGHT);
		Graphics gBuf=offScreenImage.getGraphics();
		paintComponent(gBuf);
		g.drawImage(offScreenImage,0,0,null);
	}

	public void enemyEnter(){
		if(cnt>=20) {
			enemy.add(new Enemy());
			--sum;
			cnt=0;
		}
		++cnt;
	}

	public void bossEnter(){
		if(!bossAppeared) {
			boss=new Boss();
			bossAppeared=true;
			appearTime=System.currentTimeMillis();
		}
	}

	public boolean hittedHero(){
		for(int i=0;i<enemy.size();++i){
			Enemy e=enemy.get(i);
			if (e.isLive()&&e.hitted(hero)) {
				e.injure();
				return true;
			}
		}
		return false;
	}

	public void againstEnemy(){
		if(enemy.size()>0&&hero.isLive()) {
			if(hittedHero()){
				hero.injure(1);
				++score;
			}
			score+=hero.hitted(enemy);
			if (!hero.isLive())
				state = GAMEOVER;
		}
	}

	public void againstBoss(){
		if(boss.hitted(hero))
			hero.injure(1);
		if(hero.hitted(boss))
			boss.injure(hero.power);
		if (!hero.isLive())
			state=GAMEOVER;
		else if(!boss.isLive()) {
			spendTime=(int)(System.currentTimeMillis()-appearTime)/1000;
			if((spendTime)<5)
				score+=20;
			else if((spendTime)<10)
				score+=10;
			else score+=5;
			state=WIN;
		}
	}

	//一个线程一直更新，画的时候调用的函数里物体的绘画函数，会异步进行
	class UpdateThread implements Runnable {
		GamePanel gp;
		boolean flag;//遇到游戏结束，true打开胜利窗口，false打开击破窗口

		public UpdateThread(){}
		public UpdateThread(GamePanel gp){
			this.gp=gp;
		}

		@Override
		public void run() {
			while(true) {
				switch (state) {
					case RUNNING:
						if (sum > 0)
							enemyEnter();
						else {
							bossEnter();
							boss.attack();
							againstBoss();
						}
						break;
					case GAMEOVER:
						state=CLOSING;
						flag=false;
						break;
					case WIN:
						state=CLOSING;
						flag=true;
						break;
					case CLOSED:
						if (flag) {
							JOptionPane.showMessageDialog(gp, "恭喜获得本次游戏的胜利！\n分数："+score, "游戏结束", JOptionPane.INFORMATION_MESSAGE);
							new StartFrame().setVisible(true);
							gf.dispose();
						} else {
							JOptionPane.showMessageDialog(gp, "玩家被击破\n分数："+score, "游戏结束", JOptionPane.INFORMATION_MESSAGE);
							new StartFrame().setVisible(true);
							gf.dispose();
						}
						return;
				}
				hero.attack();
				againstEnemy();
				gp.repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class KeyMonitor extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			hero.act(e);
		}
		@Override
		public void keyReleased(KeyEvent e) {
			hero.stop(e);
		}
	}
}