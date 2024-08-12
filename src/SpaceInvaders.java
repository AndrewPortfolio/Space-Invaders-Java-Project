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
    ArrayList<Block> aliens;
    int alienWidth = tileSize*2;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;



    Timer gameLoop;

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

        //game timer (updates the ship image every 60 fps)
        gameLoop = new Timer(1000/60, this); // 1000/60 = 16.67 ms => 60fps ('this' refers to spaceInvaders class)
        gameLoop.start(); //starts the timer aka the updates every 60fps
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){//used to draw everything, the ship and the aliens etc. 
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null); //this funciton has 6 parameters
                                                                                       //we only need 5 so put null for 6th one
    }

    @Override
    public void actionPerformed(ActionEvent e) { //every 60 fps we are calling the repaint() fucntion over and over again
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {} // don't need this for this game. what it does is allows us to know what char we type on the keyboard

    @Override
    public void keyPressed(KeyEvent e) {} //not using, but basically as long as a key is pressed the action will happen i.e. hold down left arrow ship will keep on moving left

    @Override
    public void keyReleased(KeyEvent e) { // oppoiste of keyPressed so u can't hold down, have to continuously press the key for action to happen i.e. hold down left arrow only moves one tile to the left
        if(e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0){//if key pressed is left arrow and ship is not at the end of the screen
            ship.x -= shipVelocityX; //moves left one tile
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width +shipVelocityX <= boardWidth){//if key pressed is right arrow and ship is not at the end of the screen
            ship.x += shipVelocityX; //moves right one tile
        }
    }
}
