import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class dyingcharacter {
    
    GamePanel gp;
    public int x, y;
    public BufferedImage image;
    public String[] dialogues = new String[5];
    public boolean dialogueFinished = false; // Tracks if the dialogue has ended
    
    public dyingcharacter(GamePanel gp) {
        this.gp = gp;
        getCharacterImage();
        setDialogue();
    }
    
    public void getCharacterImage() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/assets/tiles/dyingcharacter.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setDialogue() {
        dialogues[0] = "Ugh... another victim of the Oracle...";
        dialogues[1] = "I tried completing all the Oracle's challenges, but I failed...";
        dialogues[2] = "I just crawled into this empty room to die...";
        dialogues[3] = "I'm about to pass on... I'll only hold you back if I stay...";
        dialogues[4] = "Please... finish what I couldn't...";
    }
    
    public void draw(Graphics2D g2) {
        g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}