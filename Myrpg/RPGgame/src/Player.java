import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Player extends Entity {
    
    GamePanel gp;
    KeyHandler keyH;

    public int x, y;
    public int speed;
    public String direction = "down";

    public Rectangle solidArea;
    public boolean collisionOn = false;

    // Sprite variables
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);
        this.gp = gp;
        this.keyH = keyH;

        // Player starting position and collision box
        x = gp.tileSize * 7;
        y = gp.tileSize * 6;
        speed = 4;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        getPlayerImage();
    }

    public void checkMapTransition() {
        if (gp.tileM.currentMap == 3) {
            // MAP 3: Village transitions - Check RIGHT edge path to Map 5
            if (x > gp.tileSize * (gp.maxScreenCol - 1)) {
                int row = y / gp.tileSize;
                if (row >= 7 && row <= 8) { 
                    boolean map6Finished = gp.ui.glyphSolved[2] && gp.ui.glyphSolved[3] && gp.ui.glyphSolved[4] && gp.ui.glyphSolved[5];
                    
                    if (map6Finished) {
                        // ONLY teleport to Map 5 if Map 6 is fully finished
                        gp.tileM.currentMap = 5;
                        x = gp.tileSize * 1; // Spawn safely inside Map 5
                    } else {
                        // HARD STOP: Force player back inside Map 3 so they can't slip through
                        x = gp.tileSize * (gp.maxScreenCol - 2); 
                        collisionOn = true;
                        
                        // Trigger the warning dialogue only if it isn't already active
                        if (gp.gameState != gp.dialogueState) {
                            gp.gameState = gp.dialogueState;
                            gp.ui.currentDialogue = "The right path is sealed by a powerful barrier!\nYou must finish the glyphs in Map 6 first!";
                        }
                    }
                } else {
                    x = gp.tileSize * (gp.maxScreenCol - 2); // Block movement outside the path opening
                }
            }
            // Walk off left path (if used for Map 9 or something else)
            else if (x < 0) {
                gp.tileM.currentMap = 9;
                x = gp.tileSize;
            }
            // Walk off top path -> Map 6 (Requires earthquake puzzle to be solved)
            else if (y < 0) {
                int col = x / gp.tileSize;
                if (col >= 7 && col <= 8) {
                    if (gp.earthquakePuzzleSolved) {
                        gp.tileM.currentMap = 6;
                        y = gp.tileSize * 2; // Safely spawn inside Map 6 away from the top edge
                    } else {
                        y = gp.tileSize * 2; // Push player safely down into the map so dialogue triggers cleanly without looping
                        String[] lockedMsg = {
                            "The way north is sealed by a mysterious barrier!",
                            "You must solve the Earthquake Glyph puzzle first."
                        };
                        gp.ui.startNPCDialogue("System", lockedMsg);
                    }
                } else {
                    y = 0; // Block movement if gate isn't aligned
                }
            }
        }
        // Return to Village from Top Map (Map 6)
        else if (gp.tileM.currentMap == 6) {
            if (y > gp.tileSize * (gp.maxScreenRow - 1)) {
                gp.tileM.currentMap = 3;
                y = gp.tileSize; 
            }
        }
        // Return to Village from Left Map (Map 8 - if used elsewhere)
        else if (gp.tileM.currentMap == 8 && x > gp.tileSize * (gp.maxScreenCol - 1)) {
            gp.tileM.currentMap = 3;
            x = gp.tileSize;
        }
        // Return to Village from Right Map (Map 9)
        else if (gp.tileM.currentMap == 9 && x < 0) {
            gp.tileM.currentMap = 3;
            x = gp.tileSize * (gp.maxScreenCol - 2);
        }
        // Return to Village from Map 5 (Typhoon Corridor)
        else if (gp.tileM.currentMap == 5 && x < 0) {
            gp.tileM.currentMap = 3;
            x = gp.tileSize * (gp.maxScreenCol - 2);
        }

        // Check for map transition on Map 5
if (gp.tileM.currentMap == 5) {
    int playerCol = x / gp.tileSize;
    int playerRow = y / gp.tileSize;
    
    // Check if player is standing on the green grass exit tile at the bottom right
    if (playerCol >= 43 && playerCol <= 46 && playerRow >= 11) {
        gp.tileM.currentMap = 7;           // Switch to your new Map 7
        x = gp.tileSize * 7;               // Spawn player at X coordinate on Map 7
        y = gp.tileSize * 2;               // Spawn player at Y coordinate on Map 7
        
        // Trigger Map 7 Oracle Event & Close Entrance
        if (!gp.map7EventDone) {
            gp.gameState = gp.gameState = gp.dialogueState; // Pause game for dialogue
            gp.ui.currentDialogue = "Oracle: A severe typhoon is approaching! Quick, find shelter before the floodwaters rise!";
            
            // Close the entrance path on Map 7 (turn entrance tiles into solid trees, Tile index 1)
            gp.tileM.mapTileNum[7][7][0] = 1;
            gp.tileM.mapTileNum[7][8][0] = 1;
            
            gp.map7EventDone = true; // Ensure this only triggers once
        }
    }
}
    }
 
    public void getPlayerImage() {
        down1 = loadPlayerSprite("/assets/player/player_down.png");
        down2 = down1; 
        
        up1 = loadPlayerSprite("/assets/player/player_up.png");
        up2 = up1;
        
        left1 = loadPlayerSprite("/assets/player/player_left.png");
        left2 = left1;
        
        right1 = loadPlayerSprite("/assets/player/player_right.png");
        right2 = right1;
    }

    private BufferedImage loadPlayerSprite(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) return ImageIO.read(is);
            
            String cleanPath = path.startsWith("/") ? path.substring(1) : path;
            File f1 = new File(cleanPath);
            if (f1.exists()) return ImageIO.read(f1);

            File f2 = new File("src/" + cleanPath);
            if (f2.exists()) return ImageIO.read(f2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        BufferedImage fallback = new BufferedImage(gp.tileSize, gp.tileSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = fallback.createGraphics();
        g2.setColor(java.awt.Color.RED);
        g2.fillRect(8, 8, 32, 32);
        g2.dispose();
        return fallback;
    }

    public void update() {
        // Increase speed dynamically on Map 5 so the long corridor feels smooth
        if (gp.tileM.currentMap == 5) {
            speed = 2;
        } else {
            speed = 4;
        }

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            
            if (keyH.upPressed) direction = "up";
            else if (keyH.downPressed) direction = "down";
            else if (keyH.leftPressed) direction = "left";
            else if (keyH.rightPressed) direction = "right";

            collisionOn = false;
            gp.cChecker.checkTile(this);

            if (!collisionOn) {
                switch (direction) {
                    case "up": 
                        y -= speed; 
                        break;
                    case "down": 
                        y += speed; 
                        break;
                    case "left": 
                        x -= speed; 
                        break;
                    case "right": 
                        // Intercept right movement on Map 3 at the barrier path rows if glyphs aren't done
                        if (gp.tileM.currentMap == 3 && x + speed >= gp.tileSize * gp.maxScreenCol) {
                            int row = y / gp.tileSize;
                            if (row >= 7 && row <= 8) {
                                boolean map6Finished = gp.ui.glyphSolved[2] && gp.ui.glyphSolved[3] && gp.ui.glyphSolved[4] && gp.ui.glyphSolved[5];
                                if (!map6Finished) {
                                    x = gp.tileSize * gp.maxScreenCol - gp.tileSize; // Hard stop at boundary
                                    break;
                                }
                            }
                        }
                        x += speed; 
                        break;
                }
            }

            checkMapTransition();

            // --- FLOOD BARRIER CHECKS ---
            // --- FLOOD WAVE BARRIER CHECKS ---
            if (gp.tileM.currentMap == 5) {
                int playerCol = x / gp.tileSize;
                

                
                // Wave 1 Barrier (Column 15 - triggers at Col 14)
                if (playerCol == 14 && !gp.barrier1Cleared && !gp.waitingForAnswer) {
                    gp.gameState = gp.dialogueState;
                    gp.activeQuestion = 1;
                    gp.waitingForAnswer = true;
                    gp.ui.currentDialogue = "Incoming Flood Wave 1!\nWhat should you do during a flash flood?\n[Press 1] Stay in low areas\n[Press 2] Move to higher ground";
                }
                
                // Wave 2 Barrier (Column 25 - triggers at Col 24)
                else if (playerCol == 24 && !gp.barrier2Cleared && !gp.waitingForAnswer) {
                    gp.gameState = gp.dialogueState;
                    gp.activeQuestion = 2;
                    gp.waitingForAnswer = true;
                    gp.ui.currentDialogue = "Incoming Flood Wave 2!\nShould you walk through moving flood water?\n[Press 1] Yes, if it's shallow\n[Press 2] No, it can sweep you away";
                }
                
                // Wave 3 Barrier (Column 35 - triggers at Col 34)
                else if (playerCol == 34 && !gp.barrier3Cleared && !gp.waitingForAnswer) {
                    gp.gameState = gp.dialogueState;
                    gp.activeQuestion = 3;
                    gp.waitingForAnswer = true;
                    gp.ui.currentDialogue = "Incoming Flood Wave 3!\nWhat is the safest utility to turn off?\n[Press 1] Main power breaker\n[Press 2] Leave everything on";
                }
            }
            // -----------------------------

            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) spriteNum = 2;
                else if (spriteNum == 2) spriteNum = 1;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = down1;

        switch (direction) {
            case "up": image = (spriteNum == 1) ? up1 : up2; break;
            case "down": image = (spriteNum == 1) ? down1 : down2; break;
            case "left": image = (spriteNum == 1) ? left1 : left2; break;
            case "right": image = (spriteNum == 1) ? right1 : right2; break;
        }

        int screenX = x;

        // If on Map 5, adjust player screen position based on the scrolling camera offset
        if (gp.tileM.currentMap == 5) {
            int maxCols = gp.tileM.mapTileNum[5].length;
            int cameraX = x - (gp.maxScreenCol / 2) * gp.tileSize;
            
            int maxCameraX = (maxCols * gp.tileSize) - (gp.maxScreenCol * gp.tileSize);
            if (cameraX < 0) cameraX = 0;
            if (cameraX > maxCameraX) cameraX = maxCameraX;
            
            screenX = x - cameraX;
        }

        g2.drawImage(image, screenX, y, gp.tileSize, gp.tileSize, null);
    }
}