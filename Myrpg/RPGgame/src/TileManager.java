import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][][] mapTileNum; // [mapIndex][col][row]
    public int currentMap = 0;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[25];
        mapTileNum = new int[10][gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMaps();
    }

    public void getTileImage() {
        tile[20] = new Tile();
        tile[20].image = loadTexture("/assets/tiles/water.png", "assets/tiles/water.png", Color.BLUE);
        tile[20].collision = false;

        tile[0] = new Tile();
        tile[0].image = loadTexture("/assets/tiles/grass.png", "assets/tiles/grass.png", Color.GREEN);
        tile[0].collision = false;

        tile[1] = new Tile();
        tile[1].image = loadTexture("/assets/tiles/tree.png", "assets/tiles/tree.png", Color.DARK_GRAY);
        tile[1].collision = true;

        tile[2] = new Tile();
        tile[2].image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tile[2].image.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.tileSize, gp.tileSize);
        g2.dispose();
        tile[2].collision = false;

        BufferedImage rawHouse = loadTexture("/assets/tiles/house.png", "assets/tiles/house.png", Color.ORANGE);
        int subW = rawHouse.getWidth() / 3;
        int subH = rawHouse.getHeight() / 3;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int tileIndex = 3 + (row * 3) + col;
                
                tile[tileIndex] = new Tile();
                tile[tileIndex].image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
                Graphics2D gHouse = tile[tileIndex].image.createGraphics();
                
                if (tile[0].image != null) {
                    gHouse.drawImage(tile[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
                }
                
                BufferedImage slice = rawHouse.getSubimage(col * subW, row * subH, subW, subH);
                gHouse.drawImage(slice, 0, 0, gp.tileSize, gp.tileSize, null);
                gHouse.dispose();
                
                tile[tileIndex].collision = true;
            }
        }

        tile[12] = new Tile();
        tile[12].image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gPath = tile[12].image.createGraphics();
        gPath.setColor(new Color(180, 130, 80));
        gPath.fillRect(0, 0, gp.tileSize, gp.tileSize);
        gPath.dispose();
        tile[12].collision = false;

        // Added building door tile for Map 8
        tile[13] = new Tile();
        tile[13].image = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gHouse = tile[13].image.createGraphics();
        gHouse.setColor(new Color(139, 69, 19)); 
        gHouse.fillRect(0, 0, gp.tileSize, gp.tileSize);
        gHouse.setColor(Color.YELLOW); 
        gHouse.drawRect(4, 4, gp.tileSize - 8, gp.tileSize - 8);
        gHouse.dispose();
        tile[13].collision = false; 
    }

    private BufferedImage loadTexture(String resourcePath, String filePath, Color fallbackColor) {
        BufferedImage img = null;

        try {
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is != null) {
                img = ImageIO.read(is);
            }
        } catch (Exception e) {}

        if (img == null) {
            String[] paths = { filePath, "src/" + filePath, "../" + filePath };
            for (String path : paths) {
                File f = new File(path);
                if (f.exists()) {
                    try {
                        img = ImageIO.read(f);
                        break;
                    } catch (Exception e) {}
                }
            }
        }

        if (img == null) {
            img = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setColor(fallbackColor);
            g2.fillRect(0, 0, gp.tileSize, gp.tileSize);
            g2.dispose();
        }

        return img;
    }

    public void loadMaps() {
        for (int c = 0; c < gp.maxScreenCol; c++) {
            for (int r = 0; r < gp.maxScreenRow; r++) {
                if (c == 0 || c == gp.maxScreenCol - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                    mapTileNum[0][c][r] = 1;
                } else {
                    mapTileNum[0][c][r] = 0;
                }
            }
        }

        for (int c = 0; c < gp.maxScreenCol; c++) {
            for (int r = 0; r < gp.maxScreenRow; r++) {
                mapTileNum[1][c][r] = 2;
            }
        }

        for (int c = 0; c < gp.maxScreenCol; c++) {
            for (int r = 0; r < gp.maxScreenRow; r++) {
                if (c <= 3 || c >= gp.maxScreenCol - 4) {
                    mapTileNum[2][c][r] = 1;
                } else {
                    mapTileNum[2][c][r] = 0;
                }
            }
        }

        for (int c = 0; c < gp.maxScreenCol; c++) {
            for (int r = 0; r < gp.maxScreenRow; r++) {
                if (c == 0 || c == gp.maxScreenCol - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                    if (((c == 0 || c == gp.maxScreenCol - 1) && (r >= 9 && r <= 10)) ||
                        (r == 0 && (c >= 7 && c <= 8))) {
                        mapTileNum[3][c][r] = 12; 
                    } else {
                        mapTileNum[3][c][r] = 1; 
                    }
                } else {
                    mapTileNum[3][c][r] = 0; 
                }
            }
        }
        
        for (int c = 1; c < gp.maxScreenCol - 1; c++) {
            mapTileNum[3][c][9] = 12; 
            mapTileNum[3][c][10] = 12;
        }
        for (int r = 1; r < 9; r++) {
            mapTileNum[3][7][r] = 12;
            mapTileNum[3][8][r] = 12;
        }
        
        int houseCol = 3;
        int houseRow = 4;
        int currentTile = 3;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                mapTileNum[3][houseCol + c][houseRow + r] = currentTile;
                currentTile++;
            }
        }

        for (int c = 0; c < gp.maxScreenCol; c++) {
            for (int r = 0; r < gp.maxScreenRow; r++) {
                if (c == 0 || c == gp.maxScreenCol - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                    mapTileNum[4][c][r] = 1; 
                } else {
                    mapTileNum[4][c][r] = 0; 
                }
            }
        }
        for (int r = 9; r <= 10; r++) {
            mapTileNum[4][gp.maxScreenCol - 1][r] = 12; 
        }
        for (int c = 1; c < gp.maxScreenCol - 1; c++) {
            mapTileNum[4][c][9] = 12;
            mapTileNum[4][c][10] = 12;
        }

        int map5Cols = 50;
        mapTileNum[5] = new int[map5Cols][gp.maxScreenRow];

        for (int c = 0; c < map5Cols; c++) {
            for (int r = 0; r < gp.maxScreenRow; r++) {
                if (c == 0 || c == map5Cols - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                    mapTileNum[5][c][r] = 1; 
                } else {
                    mapTileNum[5][c][r] = 0; 
                }
            }
        }

        for (int r = 9; r <= 10; r++) {
            mapTileNum[5][0][r] = 12; 
        }

        

        for (int c = 0; c < gp.maxScreenCol; c++) {
            for (int r = 0; r < gp.maxScreenRow; r++) {
                if (c == 0 || c == gp.maxScreenCol - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                    mapTileNum[6][c][r] = 1;
                } else {
                    mapTileNum[6][c][r] = 0;
                }
            }
        }
        for (int c = 7; c <= 8; c++) {
            mapTileNum[6][c][gp.maxScreenRow - 1] = 12; 
        }

        // Map 7: Dying Character Room
        int map7Cols = gp.maxScreenCol;
        mapTileNum[7] = new int[map7Cols][gp.maxScreenRow];

        for (int c = 0; c < map7Cols; c++) {
            for (int r = 0; r < gp.maxScreenRow; r++) {
                if (c == 0 || c == map7Cols - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                    mapTileNum[7][c][r] = 1; 
                } else {
                    mapTileNum[7][c][r] = 0; 
                }
            }
        }
        
        for (int c = 7; c <= 8; c++) {
            mapTileNum[7][c][0] = 12; 
        }

        // Map 8: Located to the right of Map 7
        int map8Cols = gp.maxScreenCol;
        mapTileNum[8] = new int[map8Cols][gp.maxScreenRow];

        for (int c = 0; c < map8Cols; c++) {
            for (int r = 0; r < gp.maxScreenRow; r++) {
                if (c == 0 || c == map8Cols - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                    mapTileNum[8][c][r] = 1; 
                } else {
                    mapTileNum[8][c][r] = 0; 
                }
            }
        }
        // Widen left side opening for Map 8 (rows 4 to 6)
        for (int r = 4; r <= 6; r++) {
            mapTileNum[8][0][r] = 0;
        }

        // Place the building door on the right wall of Map 8 (col 14, row 5)
        mapTileNum[8][14][5] = 13;

        // --- ADD RANDOM OBSTACLE TREES IN THE MIDDLE ---
       mapTileNum[8][5][2] = 1;
        mapTileNum[8][7][4] = 1;
        mapTileNum[8][9][3] = 1;
        mapTileNum[8][11][6] = 1;
        
        // 6 Additional Random Trees
        mapTileNum[8][4][8] = 1;
        mapTileNum[8][6][2] = 1;
        mapTileNum[8][8][5] = 1;
        mapTileNum[8][10][9] = 1;
        mapTileNum[8][12][4] = 1;
        mapTileNum[8][13][8] = 1;
    }

    public void draw(Graphics2D g2) {
        if (currentMap == 5) {
            int maxCols = mapTileNum[5].length;
            int cameraX = gp.player.x - (gp.maxScreenCol / 2) * gp.tileSize;
            int maxCameraX = (maxCols * gp.tileSize) - (gp.maxScreenCol * gp.tileSize);
            if (cameraX < 0) cameraX = 0;
            if (cameraX > maxCameraX) cameraX = maxCameraX;
            
            int tileColOffset = cameraX / gp.tileSize;
            
            for (int col = 0; col < gp.maxScreenCol + 1; col++) {
                int targetCol = col + tileColOffset;
                if (targetCol >= maxCols) break;
                
                for (int row = 0; row < gp.maxScreenRow; row++) {
                    int tileNum = mapTileNum[5][targetCol][row];
                    if (tile[tileNum] != null && tile[tileNum].image != null) {
                        int x = (targetCol * gp.tileSize) - cameraX;
                        int y = row * gp.tileSize;
                        g2.drawImage(tile[tileNum].image, x, y, gp.tileSize, gp.tileSize, null);
                    }
                }
            }
        } else {
            for (int col = 0; col < gp.maxScreenCol; col++) {
                for (int row = 0; row < gp.maxScreenRow; row++) {
                    int tileNum = mapTileNum[currentMap][col][row];
                    if (tile[tileNum] != null && tile[tileNum].image != null) {
                        g2.drawImage(tile[tileNum].image, col * gp.tileSize, row * gp.tileSize, gp.tileSize, gp.tileSize, null);
                    }
                }
            }
        }

        if (currentMap == 6) {
            int playerCol = gp.player.x / gp.tileSize;
            int playerRow = gp.player.y / gp.tileSize;

            drawSingleGlyph(g2, 2, 2, 2, playerCol, playerRow);
            drawSingleGlyph(g2, 13, 2, 3, playerCol, playerRow);
            drawSingleGlyph(g2, 2, 9, 4, playerCol, playerRow);
            drawSingleGlyph(g2, 13, 9, 5, playerCol, playerRow);
        }
    }

    private void drawSingleGlyph(Graphics2D g2, int col, int row, int glyphNum, int playerCol, int playerRow) {
        int x = col * gp.tileSize;
        int y = row * gp.tileSize;

        boolean solved = gp.ui.glyphSolved[glyphNum];

        g2.setColor(solved ? new Color(0, 255, 0, 180) : new Color(255, 215, 0, 220));
        g2.fillRect(x + 8, y + 8, gp.tileSize - 16, gp.tileSize - 16);
        g2.setColor(Color.WHITE);
        g2.drawRect(x + 8, y + 8, gp.tileSize - 16, gp.tileSize - 16);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.BLACK);
        g2.drawString("G" + glyphNum, x + 14, y + 28);

        if (Math.abs(playerCol - col) <= 1 && Math.abs(playerRow - row) <= 1) {
            g2.setColor(Color.YELLOW);
            String prompt = solved ? "Glyph " + glyphNum + " Cleared" : "Press [E] for Glyph " + glyphNum;
            g2.drawString(prompt, x - 25, y - 10);
        }
    }

    public void unlockNorthGate() {
        for (int c = 7; c <= 8; c++) {
            mapTileNum[3][c][0] = 12; 
        }
    }
}