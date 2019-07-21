package shooting;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import shooting.shooting_dao;

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
        panel.setLayout(null);
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
        JLabel User = new JLabel();
        JLabel Rank1 = new JLabel();
        JLabel Rank2 = new JLabel();
        JLabel Rank3 = new JLabel();
        String GetRank[][] = new String[3][3];
        ArrayList<String> texts = new ArrayList<>();
        JTextField InputUserName;
        String UserName;
    	shooting_dao dao = new shooting_dao();	
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
            User();
        }
        
        public void User() {
			InputUserName = new JTextField("", 20);
			InputUserName.setLayout(null);
			InputUserName.setBounds(150, 200, 500, 40);
			this.add(InputUserName);
			InputUserName.addKeyListener(this);
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
        
        public void RankingLabel() {

	        	User.setOpaque(true);
	        	User.setBackground(Color.black);
	        	User = new JLabel(UserName + "さんの" + "只今の記録：" + CountTime/10 + "秒");
	        	User.setBounds(150, 100, 500, 50);
	        	User.setHorizontalAlignment(JLabel.CENTER);
	        	User.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
	        	User.setForeground(Color.white);
	        	this.add(User);
        	
	        	Rank1.setOpaque(true);
	        	Rank1.setBackground(Color.black);
	        	Rank1 = new JLabel("No,1:" + GetRank[0][0] + ":" + GetRank[0][1]);
	        	Rank1.setBounds(150, 180, 500, 50);
	        	Rank1.setHorizontalAlignment(JLabel.CENTER);
	        	Rank1.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 20));
	        	Rank1.setForeground(Color.yellow);
	        	this.add(Rank1);
	        
	        if(GetRank[1][0] != null) {
	        	Rank2.setOpaque(true);
	        	Rank2.setBackground(Color.black);
	        	Rank2 = new JLabel("No,2:" + GetRank[1][0] + ":" + GetRank[1][1]);
	        	Rank2.setBounds(150, 230, 500, 50);
	        	Rank2.setHorizontalAlignment(JLabel.CENTER);
	        	Rank2.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 20));
	        	Rank2.setForeground(new Color(128,128,128));
	        	this.add(Rank2);
	        }
	        
	        if(GetRank[2][0] != null) {
	        	Rank3.setOpaque(true);
	        	Rank3.setBackground(Color.black);
	        	Rank3 = new JLabel("No,3:" + GetRank[2][0] + ":" + GetRank[2][1]);
	        	Rank3.setBounds(150, 280, 500, 50);
	        	Rank3.setHorizontalAlignment(JLabel.CENTER);
	        	Rank3.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 20));
	        	Rank3.setForeground(new Color(115,41,61));
	        	this.add(Rank3);
	        }
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
				 UserName = InputUserName.getText();
				 this.remove(InputUserName);
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
	        	//スタート画面
	        	dimOfPanel = getSize();
		        super.paintComponent(g);		        
		        Font font3 = new Font("HGP創英角ﾎﾟｯﾌﾟ体",Font.ITALIC,30);
		        Font font2 = new Font("HGP創英角ﾎﾟｯﾌﾟ体",Font.ITALIC,20);
				g.setFont(font3);
				g.setColor(Color.white);
				g.drawString("シューティングゲーム〜〜〜〜〜〜〜", 150, 100);
				g.drawString("Start: Enter  Exit: Shift + Q ", 200, 350);
				g.setFont(font2);
				g.setColor(Color.red);
				g.drawString("名前を入力してください↓↓↓" , 250, 180);
	        }else if(mode == 1) {
	        	this.remove(User);
	        	this.remove(Rank1);
	        	this.remove(Rank2);
	        	this.remove(Rank3);
	        	//ゲーム中
	            dimOfPanel = getSize();
	            super.paintComponent(g);
	            this.remove(InputUserName);
	            // 各要素の描画
	            drawMyPlane(g);       // 自機
	            drawMyMissile(g);     // 自機のミサイル
	            drawEnemyPlane(g);    // 敵機
	            drawEnemyMissile(g);  // 敵機のミサイル
	            drawLifeMyPlane(g);	            
	        }else if(mode == 3) {
	        	//負け画面
	        	CountTime = 0.0; //タイマーリセット
	        	numOfAlive = 12; //敵機リセット
	        	this.remove(label_time); //タイマーラベル取り除き
	        	this.remove(InputUserName);
	        	TimeWatch.stop(); //タイマー停止
	        	timer.stop(); //描画停止
	        	setEnabled(true);; //パネルでキーを受付ける
	        	dimOfPanel = getSize();
		        super.paintComponent(g);
		        Font font3 = new Font("HGP創英角ﾎﾟｯﾌﾟ体",Font.ITALIC,30);
				g.setFont(font3);
				g.setColor(Color.white);
				g.drawString("ざんね〜〜〜〜〜〜〜〜〜〜〜〜ん", 150, 150);
				g.setColor(Color.white);
				g.drawString("Start: Enter  Exit: Shift + Q ", 200, 330);
				System.out.println(UserName);
	        }else if(mode == 4) {
	        	//勝利画面
	        	numOfAlive = 12;
	        	this.remove(label_time);
	        	this.remove(InputUserName);
	        	TimeWatch.stop();
	        	timer.stop();
	        	setEnabled(true);; //パネルでキーを受付ける
	        	dimOfPanel = getSize();
		        super.paintComponent(g);
		        Font font3 = new Font("HGP創英角ﾎﾟｯﾌﾟ体",Font.ITALIC,30);
				g.setFont(font3);
				g.setColor(Color.white);
				g.drawString("おめでとうございます〜〜〜〜〜〜", 150, 75);
				g.setColor(Color.white);
				
				if(UserName.isEmpty()) {
					UserName = "名無し";
				}
				WriteRank();
	        	GetRanking();
	        	RankingLabel();
				CountTime = 0.0;
				g.setColor(Color.white);
				g.drawString("Restart: Enter  Exit: Shift + Q ", 200, 400);
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
        
        public void GetRanking() {
        	texts = dao.text_read();
        	GetRank  = dao.LogicData(dao.parth_text(texts));
        }
        
        public void WriteRank() {
        	dao.text_make(); //ファイル作成
        	dao.text_write(UserName, CountTime/10);
        }
       
        //不要
        public void keyTyped(KeyEvent e){}
		public void keyReleased(KeyEvent e) {}
    }
}

//ranking.setOpaque(true);
//ranking.setBackground(Color.black);
//ranking = new JLabel("aaaaaaaaaaaaaa");
//ranking.setBounds(600, 0, 200, 50);
//ranking.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 24));
//ranking.setForeground(Color.red);
//this.add(ranking);
