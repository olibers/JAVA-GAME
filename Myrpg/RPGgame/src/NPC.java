import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class NPC {

    GamePanel gp;

    public String name;
    public String[] dialogues;

    public int x, y;
    public String direction = "down";
    public boolean collision = true;

    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;

    public BufferedImage image;

    public NPC(GamePanel gp) {
        this.gp = gp;

        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        getImage();
    }

    public void getImage() {
        image = loadImage("assets/tiles/mysteriousguy.png");
        if (image == null) {
            image = createFallbackImage();
        }
    }

    private BufferedImage loadImage(String relativePath) {
        try {
            File file = new File(relativePath);
            if (file.exists()) {
                return ImageIO.read(file);
            }
            var stream = getClass().getResourceAsStream("/" + relativePath);
            if (stream != null) {
                return ImageIO.read(stream);
            }
            stream = getClass().getClassLoader().getResourceAsStream(relativePath);
            if (stream != null) {
                return ImageIO.read(stream);
            }
        } catch (Exception e) {
            // Handled by fallback
        }
        return null;
    }

    private BufferedImage createFallbackImage() {
        BufferedImage img = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.YELLOW);
        g2.fillRect(0, 0, gp.tileSize, gp.tileSize);
        g2.dispose();
        return img;
    }

    public void update() {}

    public void draw(Graphics2D g2) {

    if (image != null) {

        // ==========================================
        // ORACLE SIZE
        // ==========================================

        double oracleScale = 2;

        int width =
            (int)(gp.tileSize * oracleScale);

        int height =
            (int)(gp.tileSize * oracleScale);

        // Center horizontally
        int drawX =
            x - (width - gp.tileSize) / 2;

        // Keep feet on the original tile position
        int drawY =
            y - (height - gp.tileSize);

        g2.drawImage(
            image,
            drawX,
            drawY,
            width,
            height,
            null
        );

    } else {

        g2.setColor(Color.YELLOW);

        g2.fillRect(
            x,
            y,
            gp.tileSize,
            gp.tileSize
        );
    }
}
}