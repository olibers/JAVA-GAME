import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class BookItem {

    public int x, y;
    public boolean visible = false;
    public Rectangle solidArea;
    GamePanel gp;

    public BookItem(GamePanel gp, int tileX, int tileY) {
        this.gp = gp;
        this.x = tileX * gp.tileSize;
        this.y = tileY * gp.tileSize;
        this.solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
    }

    public void draw(Graphics2D g2) {
        if (!visible) return;

        // Draw guide book
        g2.setColor(new Color(180, 50, 50));
        g2.fillRect(x + 12, y + 8, gp.tileSize - 24, gp.tileSize - 16);

        // Gold trim
        g2.setColor(Color.YELLOW);
        g2.drawRect(x + 12, y + 8, gp.tileSize - 24, gp.tileSize - 16);
        g2.fillRect(x + 20, y + 16, 8, 16);
    }
}