import javax.swing.*; 

public class App {
    public static void main(String[] args) throws Exception {
       //window variables
        int tileSize = 32;
        int rows =16; 
        int columns = 16;
        int boardWidth = tileSize * columns; //32*16 = 512px
        int boardHeight = tileSize * rows; //32*16 = 512 px

        JFrame frame = new JFrame("Space Invaders"); //title that shows on the window
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(frame); 
        frame.setResizable(false); //makes it so that user can not resize the shape of the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes it so that when u hit on x it closes the window
    
        SpaceInvaders spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true); //makes the window visible


    }

}
