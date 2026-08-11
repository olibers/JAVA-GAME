import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, bookPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            // Handled by UI JOptionPane currently
        } 
        // PLAY STATE
        else if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                upPressed = true;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                downPressed = true;
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                leftPressed = true;
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                rightPressed = true;
            }
            if (code == KeyEvent.VK_E || code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
            if (code == KeyEvent.VK_B) {
                // Toggle guidebook on/off
                bookPressed = !bookPressed;
            }
        }
       // DIALOGUE STATE (Handles NPCs and Flood Wave Questions)
else if (gp.gameState == gp.dialogueState) {
    if (gp.waitingForAnswer) {
        // Question 1 Answers (Correct is 2)
        if (gp.activeQuestion == 1) {
            if (code == KeyEvent.VK_2 || code == KeyEvent.VK_NUMPAD2) {
                gp.barrier1Cleared = true;
                gp.waitingForAnswer = false;
                gp.gameState = gp.playState;
            } else if (code == KeyEvent.VK_1 || code == KeyEvent.VK_NUMPAD1) {
                // --- WRONG ANSWER RESET ---
                gp.barrier1Cleared = false;
                gp.barrier2Cleared = false;
                gp.barrier3Cleared = false;
                gp.barrier4Cleared = false;
                gp.barrier5Cleared = false;
                gp.map5FloodTriggered = false;
                gp.map5FloodRunning = false;
                gp.waitingForAnswer = false;
                gp.gameState = gp.playState;
                gp.player.x = 2 * gp.tileSize; // Send back to start

                // Reset Map 5 tiles back to grass & re-add barriers
                int map5Cols = 50;
                for (int c = 0; c < map5Cols; c++) {
                    for (int r = 0; r < gp.maxScreenRow; r++) {
                        if (c == 0 || c == map5Cols - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                            gp.tileM.mapTileNum[5][c][r] = 1;
                        } else {
                            gp.tileM.mapTileNum[5][c][r] = 0;
                        }
                    }
                }
                for (int r = 9; r <= 10; r++) {
                    gp.tileM.mapTileNum[5][0][r] = 12; 
                }
                for (int r = 1; r < gp.maxScreenRow - 1; r++) {
                    gp.tileM.mapTileNum[5][10][r] = 20;
                    gp.tileM.mapTileNum[5][15][r] = 20;
                    gp.tileM.mapTileNum[5][25][r] = 20;
                    gp.tileM.mapTileNum[5][35][r] = 20;
                    gp.tileM.mapTileNum[5][40][r] = 20;
                }
            }
        }
        
        // Question 2 Answers (Correct is 2)
        else if (gp.activeQuestion == 2) {
            if (code == KeyEvent.VK_2 || code == KeyEvent.VK_NUMPAD2) {
                gp.barrier2Cleared = true;
                gp.waitingForAnswer = false;
                gp.gameState = gp.playState;
            } else if (code == KeyEvent.VK_1 || code == KeyEvent.VK_NUMPAD1) {
                // --- WRONG ANSWER RESET ---
                gp.barrier1Cleared = false;
                gp.barrier2Cleared = false;
                gp.barrier3Cleared = false;
                gp.barrier4Cleared = false;
                gp.barrier5Cleared = false;
                gp.map5FloodTriggered = false;
                gp.map5FloodRunning = false;
                gp.waitingForAnswer = false;
                gp.gameState = gp.playState;
                gp.player.x = 2 * gp.tileSize;

                int map5Cols = 50;
                for (int c = 0; c < map5Cols; c++) {
                    for (int r = 0; r < gp.maxScreenRow; r++) {
                        if (c == 0 || c == map5Cols - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                            gp.tileM.mapTileNum[5][c][r] = 1;
                        } else {
                            gp.tileM.mapTileNum[5][c][r] = 0;
                        }
                    }
                }
                for (int r = 9; r <= 10; r++) {
                    gp.tileM.mapTileNum[5][0][r] = 12; 
                }
                for (int r = 1; r < gp.maxScreenRow - 1; r++) {
                    gp.tileM.mapTileNum[5][10][r] = 20;
                    gp.tileM.mapTileNum[5][15][r] = 20;
                    gp.tileM.mapTileNum[5][25][r] = 20;
                    gp.tileM.mapTileNum[5][35][r] = 20;
                    gp.tileM.mapTileNum[5][40][r] = 20;
                }
            }
        }
        
        // Question 3 Answers (Correct is 1)
        else if (gp.activeQuestion == 3) {
            if (code == KeyEvent.VK_1 || code == KeyEvent.VK_NUMPAD1) {
                gp.barrier3Cleared = true;
                gp.waitingForAnswer = false;
                gp.gameState = gp.playState;
            } else if (code == KeyEvent.VK_2 || code == KeyEvent.VK_NUMPAD2) {
                // --- WRONG ANSWER RESET ---
                gp.barrier1Cleared = false;
                gp.barrier2Cleared = false;
                gp.barrier3Cleared = false;
                gp.barrier4Cleared = false;
                gp.barrier5Cleared = false;
                gp.map5FloodTriggered = false;
                gp.map5FloodRunning = false;
                gp.waitingForAnswer = false;
                gp.gameState = gp.playState;
                gp.player.x = 2 * gp.tileSize;

                int map5Cols = 50;
                for (int c = 0; c < map5Cols; c++) {
                    for (int r = 0; r < gp.maxScreenRow; r++) {
                        if (c == 0 || c == map5Cols - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                            gp.tileM.mapTileNum[5][c][r] = 1;
                        } else {
                            gp.tileM.mapTileNum[5][c][r] = 0;
                        }
                    }
                }
                for (int r = 9; r <= 10; r++) {
                    gp.tileM.mapTileNum[5][0][r] = 12; 
                }
                for (int r = 1; r < gp.maxScreenRow - 1; r++) {
                    gp.tileM.mapTileNum[5][10][r] = 20;
                    gp.tileM.mapTileNum[5][15][r] = 20;
                    gp.tileM.mapTileNum[5][25][r] = 20;
                    gp.tileM.mapTileNum[5][35][r] = 20;
                    gp.tileM.mapTileNum[5][40][r] = 20;
                }
            }
        }

        // Question 4 Answers (Correct is 1)
        else if (gp.activeQuestion == 4) {
            if (code == KeyEvent.VK_1 || code == KeyEvent.VK_NUMPAD1) {
                gp.barrier4Cleared = true;
                gp.waitingForAnswer = false;
                gp.gameState = gp.playState;
            } else if (code == KeyEvent.VK_2 || code == KeyEvent.VK_NUMPAD2) {
                // --- WRONG ANSWER RESET ---
                gp.barrier1Cleared = false;
                gp.barrier2Cleared = false;
                gp.barrier3Cleared = false;
                gp.barrier4Cleared = false;
                gp.barrier5Cleared = false;
                gp.map5FloodTriggered = false;
                gp.map5FloodRunning = false;
                gp.waitingForAnswer = false;
                gp.gameState = gp.playState;
                gp.player.x = 2 * gp.tileSize;

                int map5Cols = 50;
                for (int c = 0; c < map5Cols; c++) {
                    for (int r = 0; r < gp.maxScreenRow; r++) {
                        if (c == 0 || c == map5Cols - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                            gp.tileM.mapTileNum[5][c][r] = 1;
                        } else {
                            gp.tileM.mapTileNum[5][c][r] = 0;
                        }
                    }
                }
                for (int r = 9; r <= 10; r++) {
                    gp.tileM.mapTileNum[5][0][r] = 12; 
                }
                for (int r = 1; r < gp.maxScreenRow - 1; r++) {
                    gp.tileM.mapTileNum[5][10][r] = 20;
                    gp.tileM.mapTileNum[5][15][r] = 20;
                    gp.tileM.mapTileNum[5][25][r] = 20;
                    gp.tileM.mapTileNum[5][35][r] = 20;
                    gp.tileM.mapTileNum[5][40][r] = 20;
                }
            }
        }
        
        // Question 5 Answers (Correct is 1)
        else if (gp.activeQuestion == 5) {
            if (code == KeyEvent.VK_1 || code == KeyEvent.VK_NUMPAD1) {

                // Correct answer
                gp.barrier5Cleared = true;
                gp.waitingForAnswer = false;
                gp.gameState = gp.playState;

                // ALL 5 QUESTIONS ARE CORRECT
                if (gp.barrier1Cleared &&
                    gp.barrier2Cleared &&
                    gp.barrier3Cleared &&
                    gp.barrier4Cleared &&
                    gp.barrier5Cleared) {

                    gp.openMap5ExitPath();
                }

            } else if (code == KeyEvent.VK_2 || code == KeyEvent.VK_NUMPAD2) {
                // --- WRONG ANSWER RESET ---
                gp.barrier1Cleared = false;
                gp.barrier2Cleared = false;
                gp.barrier3Cleared = false;
                gp.barrier4Cleared = false;
                gp.barrier5Cleared = false;
                gp.map5FloodTriggered = false;
                gp.map5FloodRunning = false;
                gp.waitingForAnswer = false;
                gp.gameState = gp.playState;
                gp.player.x = 2 * gp.tileSize;

                int map5Cols = 50;
                for (int c = 0; c < map5Cols; c++) {
                    for (int r = 0; r < gp.maxScreenRow; r++) {
                        if (c == 0 || c == map5Cols - 1 || r == 0 || r == gp.maxScreenRow - 1) {
                            gp.tileM.mapTileNum[5][c][r] = 1;
                        } else {
                            gp.tileM.mapTileNum[5][c][r] = 0;
                        }
                    }
                }
                for (int r = 9; r <= 10; r++) {
                    gp.tileM.mapTileNum[5][0][r] = 12; 
                }
                for (int r = 1; r < gp.maxScreenRow - 1; r++) {
                    gp.tileM.mapTileNum[5][10][r] = 20;
                    gp.tileM.mapTileNum[5][15][r] = 20;
                    gp.tileM.mapTileNum[5][25][r] = 20;
                    gp.tileM.mapTileNum[5][35][r] = 20;
                    gp.tileM.mapTileNum[5][40][r] = 20;
                }
            }
        }
    } else {
        // Normal NPC Dialogue progression
        if (code == KeyEvent.VK_E || code == KeyEvent.VK_ENTER) {
            gp.ui.advanceNPCDialogue();
        }
    }
}

        
        // PUZZLE STATE (Earthquake & All Glyphs)
        else if (gp.gameState == gp.puzzleState) {
            gp.ui.handleInput(code);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (code == KeyEvent.VK_E || code == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }
    }
}