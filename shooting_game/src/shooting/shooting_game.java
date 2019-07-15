package shooting;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;


public class shooting_game extends JFrame {
    final int windowWidth = 800;
    final int windowHeight = 500;

    public static void main(String[] args){
        new shooting_game();
    }

    public shooting_game() {
        Dimension dimOfScreen = 
               Toolkit.getDefaultToolkit().getScreenSize();

        setBounds(dimOfScreen.width/2 - windowWidth/2, 
                  dimOfScreen.height/2 - windowHeight/2, 
                  windowWidth, windowHeight);
        setResizable(false);
        setTitle("Software Development II");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        MyJPanel panel= new MyJPanel();
        Container c = getContentPane();
        c.add(panel);
        setVisible(true);
    }

    public class MyJPanel extends JPanel implements 
       ActionListener,KeyListener {
        /* 全体の設定に関する変数 */
        Dimension dimOfPanel;
        Timer timer , TimeWatch;
        ImageIcon iconMe, iconEnemy;
        Image imgMe, imgMeLife , imgEnemy;
        JLabel label_time = new JLabel();
        double CountTime = 0.0;

        /* 自機に関する変数 */
        int myHeight, myWidth;
        int myLifeX , myLifeY;
        int myLifePoint;
        int myX, myY, tempMyX;
        int gap = 100;
        int myMissileCount = 5;
        int myMissileX[] = new int[myMissileCount];
        int myMissileY[] = new int[myMissileCount];;
        boolean[] isMyMissileActive = new boolean[myMissileCount];;

        /* 敵機に関する変数 */
        int numOfEnemy = 12;
        int numOfAlive = numOfEnemy;
        int enemyWidth, enemyHeight;
        int[] enemyX = new int[numOfEnemy];
        int[] enemyY = new int[numOfEnemy];
        int[] enemyMove = new int[numOfEnemy];
        int[] enemyMissileX = new int[numOfEnemy];
        int[] enemyMissileY = new int[numOfEnemy];
        int[] enemyMissileSpeed = new int[numOfEnemy];
        boolean[] isEnemyAlive = new boolean[numOfEnemy];
        boolean[] isEnemyMissileActive = 
                                 new boolean[numOfEnemy];
        int mode = 0;

        /* コンストラクタ（ゲーム開始時の初期化）*/
        public MyJPanel() {
        	// 全体の設定
            setBackground(Color.black);
            setFocusable(true); //パネルでキーを受付ける
            addKeyListener(this);            
        }
        
        public void run() {            
            // 画像の取り込み
            imgMe = getImg("src/img/jiki.jpg");
            myWidth = imgMe.getWidth(this);
            myHeight = imgMe.getHeight(this);

            imgEnemy = getImg("src/img/teki.jpg");
            enemyWidth = imgEnemy.getWidth(this);
            enemyHeight = imgEnemy.getHeight(this);
            
            imgMeLife  = getImg("src/img/jiki.jpg");
            myLifeX = windowWidth - 150;
            myLifeY = windowHeight - 60;
            myLifePoint = 3;
            
            timer = new Timer(50, this);
            timer.start();
            
            //タイマーの実装
            timewatch();
            TimeWatch = new Timer(100,new myListener());
            TimeWatch.start();
           
            // 自機と敵機の初期化
            initMyPlane();
            initEnemyPlane();       
        }
        
        public void timewatch() {
        	this.remove(label_time);
        	label_time.setOpaque(true);
        	label_time.setBackground(Color.black);
            label_time = new JLabel("TIME：" + String.format("%.1f", CountTime/10));
            label_time.setBounds(600, 0, 200, 50);
            label_time.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
            label_time.setForeground(Color.red);
            this.add(label_time);
        }
        
        public class myListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				CountTime++;
				timewatch();
			}
        }
    
		public void keyPressed(KeyEvent e){
			int keycode = e.getKeyCode();
			//ゲームスタート
			  if (keycode == KeyEvent.VK_ENTER && mode == 0){
				 mode = 1;
				 run();
			  }else if(keycode == KeyEvent.VK_ENTER && mode == 3) {
				 mode = 1;
				 run();
			  }else if(keycode == KeyEvent.VK_ENTER && mode == 4) {
				  mode = 1;
				  run();
			  }
			 //終わらせる時
     		  int mod = e.getModifiersEx();
			  if ((mod & InputEvent.SHIFT_DOWN_MASK) != 0 && 
					  keycode == KeyEvent.VK_Q && mode == 0){
				  System.exit(0);
			  }else if((mod & InputEvent.SHIFT_DOWN_MASK) != 0 && 
					  keycode == KeyEvent.VK_Q && mode == 3) {
				  System.exit(0);
			  }else if ((mod & InputEvent.SHIFT_DOWN_MASK) != 0 && 
					  keycode == KeyEvent.VK_Q && mode == 4) {
				  System.exit(0);
			  }
			  //ミサイル発射
				  if(keycode == KeyEvent.VK_RIGHT) {
					  myX = myX + 30;
				  }else if(keycode == KeyEvent.VK_LEFT) {
					 myX = myX - 30;
				  }else if(keycode == KeyEvent.VK_SPACE) {
					  for (int i=0; i < myMissileCount; i++) {
						  if (!isMyMissileActive[i]) {
				                myMissileX[i] = tempMyX + myWidth/2;
				                myMissileY[i] = myY;
				                isMyMissileActive[i] = true;
				                break;
				          }
			          }
				  }
			 
		}		

        /* パネル上の描画 */
        public void paintComponent(Graphics g) {
	        if(mode == 0) {
	        	dimOfPanel = getSize();
		        super.paintComponent(g);
		        Font font3 = new Font("HGP創英角ﾎﾟｯﾌﾟ体",Font.ITALIC,30);
				g.setFont(font3);
				g.setColor(Color.white);
				g.drawString("シューティングゲーム〜〜〜〜〜〜〜", 150, 150);
				g.setColor(Color.white);
				g.drawString("Start: Enter  Exit: Shift + Q ", 200, 350);
	        }else if(mode == 1) {
	            dimOfPanel = getSize();
	            super.paintComponent(g);
	            // 各要素の描画
	            drawMyPlane(g);       // 自機
	            drawMyMissile(g);     // 自機のミサイル
	            drawEnemyPlane(g);    // 敵機
	            drawEnemyMissile(g);  // 敵機のミサイル
	            drawLifeMyPlane(g);	            
	        }else if(mode == 3) {
	        	numOfAlive = 12;
	        	this.remove(label_time);
	        	TimeWatch.stop();
	        	timer.stop();
	        	setEnabled(true);; //パネルでキーを受付ける
	        	dimOfPanel = getSize();
		        super.paintComponent(g);
		        Font font3 = new Font("HGP創英角ﾎﾟｯﾌﾟ体",Font.ITALIC,30);
				g.setFont(font3);
				g.setColor(Color.white);
				g.drawString("ざんね〜〜〜〜〜〜〜〜〜〜〜〜ん", 150, 150);
				g.setColor(Color.white);
				g.drawString("Start: Enter  Exit: Shift + Q ", 200, 350);
	        }else if(mode == 4) {
	        	numOfAlive = 12;
	        	this.remove(label_time);
	        	TimeWatch.stop();
	        	timer.stop();
	        	setEnabled(true);; //パネルでキーを受付ける
	        	dimOfPanel = getSize();
		        super.paintComponent(g);
		        Font font3 = new Font("HGP創英角ﾎﾟｯﾌﾟ体",Font.ITALIC,30);
				g.setFont(font3);
				g.setColor(Color.white);
				g.drawString("おめでとうございます〜〜〜〜〜〜", 150, 150);
				g.setColor(Color.white);
				g.drawString("Start: Enter  Exit: Shift + Q ", 200, 350);
	        }
            // 敵機のスピード変更
            if (numOfAlive == 0) {
            	mode = 4;
            }else if(numOfAlive <= 8) {
            	timer.stop();
            	timer = new Timer(25, this);
            	timer.restart();
            }
        }
        
        /* 一定時間ごとの処理（ActionListener に対する処理）*/
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
        
        /* 画像ファイルから Image クラスへの変換 */
        public Image getImg(String filename) {
            ImageIcon icon = new ImageIcon(filename);
            Image img = icon.getImage();
            return img;
        }

        /* 自機の初期化 */
        public void initMyPlane() {
            myX = windowWidth / 2;
            myY = windowHeight - 100;
            tempMyX = windowWidth / 2;
            for (int i=0; i<myMissileCount; i++) {
                isMyMissileActive[i] = false;
            }
        }

        /* 敵機の初期化 */
        public void initEnemyPlane() {
            for (int i=0; i<7; i++) {
                enemyX[i] = 70*i;
                enemyY[i] = 50;
            }

            for (int i=7; i<numOfEnemy; i++) {
                enemyX[i] = 70*(i-6);
                enemyY[i] = 100;
            }

            for (int i=0; i<numOfEnemy; i++) {
                isEnemyAlive[i] = true;
                enemyMove[i] = 1;
            }

            for (int i=0; i<numOfEnemy; i++) {
                isEnemyMissileActive[i] = true;
                enemyMissileX[i] = enemyX[i] + enemyWidth/2;
                enemyMissileY[i] = enemyY[i];
                enemyMissileSpeed[i] = 10 + (i%6);
            }
        }

        /* 自機の描画 */
        public void drawMyPlane(Graphics g) {
            if (Math.abs(tempMyX - myX) < gap) {
                if (myX < 0) {
                    myX = 0;
                } else if (myX+myWidth > dimOfPanel.width) {
                    myX = dimOfPanel.width - myWidth;
                }
                tempMyX = myX;
                g.drawImage(imgMe, tempMyX, myY, this);
            } else {
                g.drawImage(imgMe, tempMyX, myY, this);
            }
            
        }

        /* 自機のミサイルの描画 */
        public void drawMyMissile(Graphics g) {
        	for (int i=0; i < myMissileCount; i++) {
        		if (isMyMissileActive[i]) {
                    // ミサイルの配置
                    myMissileY[i] -= 15;
                    g.setColor(Color.white);
                    g.fillRect(myMissileX[i], myMissileY[i], 2, 5);

                    // 自機のミサイルの敵機各機への当たり判定
                    for (int j=0; j<numOfEnemy; j++) {
                        if (isEnemyAlive[j]) {
                            if ((myMissileX[i] >= enemyX[j]) && 
                                (myMissileX[i] <= enemyX[j]+enemyWidth) && 
                                (myMissileY[i] >= enemyY[j]) && 
                                (myMissileY[i] <= enemyY[j]+enemyHeight)) {
                                isEnemyAlive[j] = false;
                                isMyMissileActive[i] = false;
                                numOfAlive--;
                            }
                        }
                    }

                    // ミサイルがウィンドウ外に出たときのミサイルの再初期化
                    if (myMissileY[i] < 0) isMyMissileActive[i] = false;
                }
				 
		    }
        }

        /* 敵機の描画 */
        public void drawEnemyPlane(Graphics g) {
            for (int i=0; i<numOfEnemy; i++) {
                if (isEnemyAlive[i]) {
                    if (enemyX[i] > dimOfPanel.width - 
                                                   enemyWidth) {
                        enemyMove[i] = -1;
                    } else if (enemyX[i] < 0) {
                        enemyMove[i] = 1;
                    }
                    enemyX[i] += enemyMove[i]*10;
                    g.drawImage(imgEnemy, enemyX[i], 
                                          enemyY[i], this);
                }
            }
        }

        /* 敵機のミサイルの描画 */
        public void drawEnemyMissile(Graphics g) {
            for (int i=0; i<numOfEnemy; i++) {
                // ミサイルの配置
                if (isEnemyMissileActive[i]) {
                    enemyMissileY[i] += enemyMissileSpeed[i];
                    g.setColor(Color.red);
                    g.fillRect(enemyMissileX[i], 
                               enemyMissileY[i], 2, 5);
                }

                // 敵機のミサイルの自機への当たり判定
                if ((enemyMissileX[i] >= tempMyX) && 
                    (enemyMissileX[i] <= tempMyX+myWidth) && 
                    (enemyMissileY[i]+5 >= myY) && 
                    (enemyMissileY[i]+5 <= myY+myHeight)) {
//                    System.exit(0);
                
                //ミサイルを敵機に戻す
                    if (isEnemyAlive[i]) {
                        enemyMissileX[i] = enemyX[i] + 
                                                 enemyWidth/2;
                        enemyMissileY[i] = enemyY[i] + 
                                                  enemyHeight;
                    }
               //ライフポイント減らす
                	myLifePoint--;
                	if(myLifePoint == 0) {
                    	mode = 3;                		
                	}

                }

                // ミサイルがウィンドウ外に出たときのミサイルの再初期化
                if (enemyMissileY[i] > dimOfPanel.height) {
                    if (isEnemyAlive[i]) {
                        enemyMissileX[i] = enemyX[i] + 
                                                 enemyWidth/2;
                        enemyMissileY[i] = enemyY[i] + 
                                                  enemyHeight;
                    } else {
                        isEnemyMissileActive[i] = false;
                    }
                }
            }
        }
        
        public void drawLifeMyPlane(Graphics g) {
	        	g.setColor(Color.white);
				g.drawString("LIFE POINT:", myLifeX - 90, myLifeY + 20);
				if(myLifePoint == 3) {
	     			g.drawImage(imgMeLife, myLifeX, myLifeY, this); 
					g.drawImage(imgMeLife, myLifeX + 40, myLifeY, this); 
					g.drawImage(imgMeLife, myLifeX + 80, myLifeY, this); 
				}else if(myLifePoint == 2) {
					g.drawImage(imgMeLife, myLifeX, myLifeY, this); 
					g.drawImage(imgMeLife, myLifeX + 40, myLifeY, this); 
				}else if(myLifePoint == 1) {
					g.drawImage(imgMeLife, myLifeX, myLifeY, this); 
				}
        }      
       
        //不要
        public void keyTyped(KeyEvent e){}
		public void keyReleased(KeyEvent e) {}
    }
}

/*  要件定義
・スタートボタン追加　→　いきなり始める。
・ライフポイント追加　→　一発はきつい
・スコア表示
・全部倒し終わったら、ボスキャラ登場させて、えぐい強い（ほぼ勝てない）
 	要件定義	*/


/*
 	初期化処理を分けて、リセット出来るように。
 	基本的にコンストラクタは、１回しか作動できないから、初期化出来るように分ける。
	４つの画面を作成。スタート画面、プレイ中、終了、ゲームオーバー
 	エンターキーを使って開始。
*/