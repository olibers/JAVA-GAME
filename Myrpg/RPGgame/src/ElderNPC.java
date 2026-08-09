import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ElderNPC {

    public int x, y;
    public String name = "Village Elder";
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public BufferedImage image;

    // 🌟 SCALE SETTINGS
    // 1.5 multiplier makes her 1.5x larger than a standard 48x48 tile (72x72)
    public double scale = 1.5; 

    public String[] introDialogue = {
        "jidsajisadjisadijasdijasd",
        "jisadjisdajidsajiasd",
        "ndsanasdhiads",
        "nidasnasdiihads"
    };

    public String[] successDialogue = {
        "HELOOOOOOOOOOO",
        "Ehuehuehue",
        "udahusduhasdhusdahisad"
    };

    GamePanel gp;

    public ElderNPC(GamePanel gp, int tileX, int tileY) {
        this.gp = gp;
        this.x = tileX * gp.tileSize;
        this.y = tileY * gp.tileSize;

        getImage();
    }

    public void getImage() {
        try {
            image = ImageIO.read(new File("assets/tiles/OldLady.png"));
        } catch (Exception e) {
            System.out.println("Could not load OldLady.png image!");
        }
    }

    public void draw(Graphics2D g2) {
        if (image != null) {
            // Calculate scaled dimensions
            int width = (int)(gp.tileSize * scale);
            int height = (int)(gp.tileSize * scale);

            // Center offset so she grows outward evenly from her tile position
            int offsetX = (gp.tileSize - width) / 2;
            int offsetY = gp.tileSize - height; // Keeps her feet on the ground

            g2.drawImage(image, x + offsetX, y + offsetY, width, height, null);
        } else {
            // Fallback shape
            g2.setColor(new Color(30, 90, 180));
            g2.fillRect(x + 8, y + 8, gp.tileSize - 16, gp.tileSize - 16);
        }
    }
}