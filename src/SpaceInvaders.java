import java.awt.*; //used for our game
import java.awt.event.*; //used for our game
import java.util.ArrayList; // used for aliens and bullets we fire
import java.util.Random; //used for to randomly select color for our aliens 
import javax.swing.*; //used for our game

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener{
    
    class Block{ //Every image drawn needs these variables 
        int x; 
        int y; 
        int width;
        int height;
        Image img;
        boolean alive = true; //used for aliens
        boolean used = false; //used for bullets

        Block(int x, int y, int width, int height, Image img){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img; 
        }
    }
   
    //board
    int tileSize = 32; 
    int rows = 16;
    int columns = 16;
    int boardWidth = tileSize * columns; //32 * 16
    int boardHeight = tileSize * rows; //32 * 16


    Image shipImg;
    Image alienImg; 
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    ArrayList<Image> alienImgArray;

    //ship
    int shipWidth = tileSize*2; //64px (2 tile size)
    int shipHeight = tileSize; //32px (1 tile size)
    int shipX = tileSize*columns/2 - tileSize; //puts the ship into the middle of the window 
    int shipY = boardHeight - tileSize*2; //places the ship to the bottomish of the screen 
    int shipVelocityX = tileSize; //ship moving speed 
    Block ship;

    //aliens
    ArrayList<Block> alienArray;
    int alienWidth = tileSize*2;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;

    int alienRows = 2; 
    int alienColumns = 3;
    int alienCount = 0; // number of aliens to defeat
    int alienVelocityX = 1; //alien moving speed

    //bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVelocityY = -10; //bullet moving speed 

    Timer gameLoop;
    int score = 0;
    boolean gameOver = false; 


    //Constructor
    SpaceInvaders(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true); //makes it so spaceInvaders listens for the key presses
        addKeyListener(this); 

        //load images

        //getClass() location where SpaceInvaders file is 
        //getResource starting from location of SpaceInvaders 
        // "./" means start looking at this folder ship.png which file to look at
        shipImg = new ImageIcon(getClass().getResource("./ship.png")).getImage(); 
        alienImg = new ImageIcon(getClass().getResource("./alien.png")).getImage(); 
        alienCyanImg = new ImageIcon(getClass().getResource("./alien-cyan.png")).getImage(); 
        alienMagentaImg = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage(); 
        alienYellowImg = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage(); 

    //Adds all of the images into an array
        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);

        //creates block object of a ship
        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);

        //creates an array for the aliens
        alienArray = new ArrayList<Block>(); 

        //creates the rectangles for the bullets
        bulletArray = new ArrayList<Block>();

        //game timer (updates the ship image every 60 fps)
        gameLoop = new Timer(1000/60, this); // 1000/60 = 16.67 ms => 60fps ('this' refers to spaceInvaders class)
        createAliens();
        gameLoop.start(); //starts the timer aka the updates every 60fps
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    //used to draw everything, the ship and the aliens etc. 
    public void draw(Graphics g){
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null); //this funciton has 6 parameters we only need 5 so put null for 6th one

        //aliens
        for(int i = 0; i < alienArray.size(); ++i){
            Block alien = alienArray.get(i);
            if(alien.alive){
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }
        
        //bullets 
        g.setColor(Color.white);
        for(int i = 0; i < bulletArray.size(); ++i){
            Block bullet = bulletArray.get(i);
            if(!bullet.used){
                //g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height); //draws hollow rectangle bullet
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);  //draws solid white rect bullet
            }
        }

        //score
        g.setColor(Color.green);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }
        else{
            g.drawString(String.valueOf(score), 10, 35);
        }
                                                                                    
    }

    public void move(){
        //aliens
        for(int i = 0; i < alienArray.size(); ++i){
            Block alien = alienArray.get(i);
            if(alien.alive){
                alien.x += alienVelocityX;

                //if laien touches the borders
                if(alien.x + alien.width >= boardWidth || alien.x <= 0){
                    alienVelocityX *= -1; //reverses signs so that it goes back in the other dir. when it hits border
                    alien.x += alienVelocityX*2;

                    //moves all aliens down by one row
                    for(int j = 0; j < alienArray.size(); ++j){
                        alienArray.get(j).y += alienHeight;
                    }
                }
                if(alien.y >= ship.y){
                    gameOver = true;
                }
            }
        }

        //bullets
        for(int i = 0; i < bulletArray.size(); ++i){
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY; 

            //bullet collision with aliens
            for(int j = 0; j < alienArray.size(); ++j){
                Block alien = alienArray.get(j);
                if(!bullet.used && alien.alive && detectCollision(bullet, alien)){
                    bullet.used = true;
                    alien.alive = false;
                    --alienCount;
                    score += 100;
                }
            }
        }

        //clear bullets
        while(bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)){
            bulletArray.remove(0); //removes the first element of the array
        }

        //next level
        if(alienCount == 0){
            //inc the number of aliens in columns and rows by 1
            score += alienColumns * alienRows * 100;
            alienColumns = Math.min(alienColumns + 1, columns/2 -2); //cap column at 16/2 -2 = 6
            alienRows = Math.min(alienRows + 1, rows - 6); //cap row at 16-6 = 10
            alienArray.clear();
            bulletArray.clear();
            alienVelocityX = 1;
            createAliens();
        }
    }

    //randomly chooses the color of the aliens
    public void createAliens() {
        Random random = new Random();
        for(int r = 0; r < alienRows; ++r){
            for(int c = 0; c < alienColumns; c++){
                int randomImgIndex = random.nextInt(alienImgArray.size());
                Block alien = new Block(
                    alienX + c*alienWidth,
                    alienY + r*alienHeight,
                    alienWidth,
                    alienHeight,
                    alienImgArray.get(randomImgIndex)
                );
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    public boolean detectCollision(Block a, Block b){
        return a.x < b.x + b.width &&  //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&  //a's top right corner passes b's top left corner
               a.y < b.y + b.height && //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) { //every 60 fps we are calling the repaint() fucntion over and over again
        move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {} // don't need this for this game. what it does is allows us to know what char we type on the keyboard

    @Override
    public void keyPressed(KeyEvent e) {} //not using, but basically as long as a key is pressed the action will happen i.e. hold down left arrow ship will keep on moving left

    @Override
    public void keyReleased(KeyEvent e) { // oppoiste of keyPressed so u can't hold down, have to continuously press the key for action to happen i.e. hold down left arrow only moves one tile to the left
        if(gameOver){ //any key to restart
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            score = 0;
            alienVelocityX = 1;
            alienColumns = 3;
            alienRows = 2;
            createAliens();
            gameLoop.start();
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0){//if key pressed is left arrow and ship is not at the end of the screen
            ship.x -= shipVelocityX; //moves left one tile
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width +shipVelocityX <= boardWidth){//if key pressed is right arrow and ship is not at the end of the screen
            ship.x += shipVelocityX; //moves right one tile
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            Block bullet = new Block(ship.x + shipWidth*15/32, ship.y, bulletWidth, bulletHeight, null); 
            bulletArray.add(bullet);
        }
    }
}
