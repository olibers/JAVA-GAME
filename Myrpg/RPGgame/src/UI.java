import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.IOException;

public class UI {

    GamePanel gp;
    public String playerName = "";
    
    // Dialogue properties
    public String currentDialogue = "";
    public String currentSpeaker = "";
    private String[] npcDialogueList;
    private int dialogueIndex = 0;

    // Typewriter effect variables
    private int charIndex = 0;
    private int counter = 0;

    // Portrait variables
    public BufferedImage oraclePortrait;

    public int oracleCursor = 0;
    public boolean isFailingOracle = false;
    public int shakeFailTimer = 0;
    public int oracleCurrentQuestion = 0;

    String[] oracleQuestions = {
        "What should you do when a typhoon warning is issued?",
        "Which of the following is most important to prepare before a typhoon?",
        "What should you do with windows and doors before a strong typhoon arrives?",
        "Which item should be included in an emergency kit?",
        "Why should you store enough clean drinking water before a typhoon?",
        "What should you do if your area is ordered to evacuate?",
        "What should you do with electrical appliances before a severe typhoon?",
        "Is deforestation one of the main causes of environmental danger?"
    };

    String[][] oracleOptions = {
        {"A. Go outside to observe the weather", "B. Stay informed and follow advisories", "C. Ignore warning if there is no rain", "D. Travel to another place immediately"},
        {"A. Emergency go-bag", "B. New clothes", "C. Video games", "D. Decorations"},
        {"A. Leave them open", "B. Remove all locks", "C. Secure and reinforce them", "D. Open them to let air inside"},
        {"A. Flashlight and extra batteries", "B. Gaming console", "C. Television", "D. Hair dryer"},
        {"A. Water services may be interrupted", "B. Water becomes cheaper", "C. It helps cool the house", "D. It prevents strong winds"},
        {"A. Wait until typhoon gets stronger", "B. Stay home and ignore the order", "C. Evacuate to the designated safe area", "D. Go to the nearest beach"},
        {"A. Leave them plugged in", "B. Unplug them if it is safe to do so", "C. Place them near windows", "D. Turn them on continuously"},
        {"A. Yes", "B. No"}
    };

    int[] oracleCorrectAnswers = {1, 0, 2, 0, 0, 2, 1, 0};
    // Feedback message variables for wrong answers
    public String feedbackMessage = "";
    public int feedbackTimer = 0;
    public int creditTimer = 0;
    public boolean gameCompleted = false;

    public UI(GamePanel gp) {

    

    

        this.gp = gp;
        
        try {
            oraclePortrait = ImageIO.read(getClass().getResourceAsStream("/assets/tiles/mysteriousguy.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    public void chooseName() {
        while (playerName == null || playerName.trim().isEmpty()) {
            String input = JOptionPane.showInputDialog(
                gp, 
                "Please enter your hero's name:", 
                "Character Creation", 
                JOptionPane.QUESTION_MESSAGE
            );

            if (input != null && !input.trim().isEmpty()) {
                playerName = input.trim();
            } else {
                JOptionPane.showMessageDialog(
                    gp, 
                    "You must enter a valid character name to start your journey!", 
                    "Name Required", 
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }

        String[] introDialogues = {
            "Where am I? This dense forest... I only came out to gather timber to repair my house.",
            "I'm completely lost. My axe is my only companion out here. I'd better chop some trees to clear a path and survive!"
        };

        startNPCDialogue(playerName, introDialogues);
    }

    public void startNPCDialogue(String speaker, String[] dialogues) {
        gp.audio.playDialogue();
        if (dialogues == null || dialogues.length == 0) {
            return;
        }

        if (speaker == null || speaker.trim().isEmpty()) {
            this.currentSpeaker = (playerName != null && !playerName.trim().isEmpty()) ? playerName : "Lumberjack";
        } else {
            this.currentSpeaker = speaker;
        }

        this.npcDialogueList = dialogues;
        this.dialogueIndex = 0;
        this.currentDialogue = dialogues[0];
        
        this.charIndex = this.currentDialogue.length();
        this.counter = 0;
        
        gp.gameState = gp.dialogueState;
    }

    public void advanceNPCDialogue() {
        dialogueIndex++;

        if (npcDialogueList != null && dialogueIndex < npcDialogueList.length) {
            currentDialogue = npcDialogueList[dialogueIndex];
            charIndex = 0;
            counter = 0;
        } else {
            dialogueIndex = 0;

            // Check if dying traveler conversation just finished
            if (currentSpeaker.equals("Dying Traveler")) {
                gp.dyingNpc.dialogueFinished = true;
            }

            // --- CHECK IF PLAYER JUST BEAT THE GAME ---
            if (gameCompleted) {
                gp.gameState = gp.creditsState;
                creditTimer = 0; 
                return; 
            }

            if (isFailureSequenceActive) {
                isFailureSequenceActive = false;
                executeFailTeleport();
            } else {
                // If we were talking to the Oracle on Map 8, open the quiz instead of returning to playState
                if (gp.tileM.currentMap == 8 && "Oracle".equals(currentSpeaker)) {
                    gp.gameState = gp.puzzleState;
                    oracleCursor = 0;
                    oracleCurrentQuestion = 0;
                    isFailingOracle = false;
                } else {
                    gp.gameState = gp.playState;
                }
            }
        }
    }

    private boolean isFailureSequenceActive = false;

    public void drawNPCDialogue(Graphics2D g2) {
        if (currentSpeaker.equals("Oracle") && oraclePortrait != null) {
            int portraitX = gp.tileSize * 1;
            int portraitY = gp.tileSize * 1;
            int portraitWidth = gp.tileSize * 6;
            int portraitHeight = gp.tileSize * 8;
            
            g2.drawImage(oraclePortrait, portraitX, portraitY, portraitWidth, portraitHeight, null);
        }

        int x = gp.tileSize * 2;
        int y = gp.screenHeight - (gp.tileSize * 4);
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 3;

        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRoundRect(x, y, width, height, 20, 20);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(x, y, width, height, 20, 20);

        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(Color.YELLOW);
        g2.drawString(currentSpeaker, x + 20, y + 35);

        if (charIndex < currentDialogue.length()) {
            counter++;
            if (counter > 2) {
                charIndex++;
                counter = 0;
            }
        }
        
        int safeIndex = Math.min(charIndex, currentDialogue.length());
        String displayedText = currentDialogue.substring(0, safeIndex);

        g2.setFont(new Font("Arial", Font.PLAIN, 15));
        g2.setColor(Color.WHITE);

        int textX = x + 20;
        int textY = y + 65;
        int maxTextWidth = width - 40; 

        drawWrappedString(g2, displayedText, textX, textY, maxTextWidth, 22);

        g2.setFont(new Font("Arial", Font.ITALIC, 12));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("[Press E to continue]", x + width - 150, y + height - 15);
    }

    private void drawWrappedString(Graphics2D g2, String text, int x, int y, int maxWidth, int lineHeight) {
        FontMetrics fm = g2.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (fm.stringWidth(currentLine + " " + word) < maxWidth) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                g2.drawString(currentLine.toString(), x, y);
                y += lineHeight;
                currentLine = new StringBuilder(word);
            }
        }

        if (currentLine.length() > 0) {
            g2.drawString(currentLine.toString(), x, y);
        }
    }

    public void drawGuideBookUI(Graphics2D g2) {
        int width = gp.tileSize * 12;
        int height = gp.tileSize * 9;
        int x = (gp.screenWidth - width) / 2;
        int y = (gp.screenHeight - height) / 2;

        g2.setColor(new Color(40, 25, 12, 240));
        g2.fillRoundRect(x, y, width, height, 25, 25);

        g2.setColor(new Color(212, 175, 55));
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x, y, width, height, 25, 25);

        g2.setFont(new Font("Serif", Font.BOLD, 20));
        g2.setColor(new Color(255, 223, 100));
        g2.drawString("DISASTER PREPAREDNESS GUIDE", x + 40, y + 45);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(x + 35, y + 55, x + width - 35, y + 55);

        g2.setFont(new Font("Arial", Font.BOLD, 15));
        g2.setColor(Color.CYAN);
        g2.drawString("DISASTER SURVIVAL TASKS:", x + 40, y + 85);

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(Color.WHITE);

        g2.drawString("[1] EARTHQUAKE PREPARATION:", x + 40, y + 120);
        String earthquakeStatus = gp.earthquakePuzzleSolved ? "    - Completed (North Gate Unlocked)" : "    - Look for the glyph.";
        g2.setColor(gp.earthquakePuzzleSolved ? Color.GREEN : Color.WHITE);
        g2.drawString(earthquakeStatus, x + 40, y + 142);

        g2.setColor(Color.WHITE);
        g2.drawString("[2] TOP ROOM CORNER GLYPHS (2, 3, 4, 5):", x + 40, y + 180);
        boolean allTopSolved = glyphSolved[2] && glyphSolved[3] && glyphSolved[4] && glyphSolved[5];
        String topStatus = allTopSolved ? "    - All Corner Glyphs Cleared!" : "    - Explore Map 6 Corners.";
        g2.setColor(allTopSolved ? Color.GREEN : Color.WHITE);
        g2.drawString(topStatus, x + 40, y + 202);

        g2.setFont(new Font("Arial", Font.ITALIC, 13));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Press [B] to close book", x + (width / 2) - 65, y + height - 20);
    }

    public int earthquakeCursor = 0;
    public boolean earthquakeSolved = false;
    public int currentQuestion = 0;
    public int puzzleTimer = 1200; 
    public final int maxPuzzleTime = 1200;

    public void resetEarthquakePuzzle() {
        earthquakeCursor = 0;
        
        currentQuestion = 0;
        puzzleTimer = maxPuzzleTime;
        isFailureSequenceActive = false;
        feedbackTimer = 0;
    }

    public void drawEarthquakePuzzle(Graphics2D g2) {
        if (!earthquakeSolved && currentQuestion < 5) {
            puzzleTimer--;
            if (puzzleTimer <= 0) {
                failPuzzlePenalty();
                return;
            }
        }

        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRect(gp.tileSize * 1, gp.tileSize * 1, gp.tileSize * 14, gp.tileSize * 10);

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(gp.tileSize * 1 + 10, gp.tileSize * 1 + 10, gp.tileSize * 14 - 20, gp.tileSize * 10 - 20);

        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("EARTHQUAKE SURVIVAL CHALLENGE", gp.tileSize * 1 + 30, gp.tileSize * 2);

        int secondsLeft = puzzleTimer / 60;
        g2.setColor(secondsLeft <= 10 ? Color.RED : Color.YELLOW);
        g2.drawString("Time Left: " + secondsLeft + "s", gp.tileSize * 10 + 20, gp.tileSize * 2);

        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.PLAIN, 15));

        if (!earthquakeSolved && currentQuestion < 5) {
            if (currentQuestion == 0) {
                g2.drawString("Q1: Arrange the proper sequence for Duck, Cover, and Hold:", gp.tileSize * 1 + 30, gp.tileSize * 2 + 45);
                String[] q1Opts = {
                    "A. Drop to the ground, take cover under a sturdy desk, hold on",
                    "B. Run outside, scream, jump up and down",
                    "C. Stand upright, look around, wait",
                    "D. Open windows, hide in a closet"
                };
                drawOptions(g2, q1Opts);
            } 
            else if (currentQuestion == 1) {
                g2.drawString("Q2: What causes earthquakes?", gp.tileSize * 1 + 30, gp.tileSize * 2 + 45);
                String[] q2Opts = {
                    "A. Movement of tectonic plates along faults",
                    "B. Heavy rainfall and strong winds",
                    "C. Melting glaciers and high tides",
                    "D. Excessive tree chopping"
                };
                drawOptions(g2, q2Opts);
            } 
            else if (currentQuestion == 2) {
                g2.drawString("Q3: You are outside when the shaking begins, stay there.", gp.tileSize * 1 + 30, gp.tileSize * 2 + 45);
                String[] q3Opts = {"A. True", "B. False"};
                drawOptions(g2, q3Opts);
            } 
            else if (currentQuestion == 3) {
                g2.drawString("Q4: The best way to prevent injuries is to be prepared.", gp.tileSize * 1 + 30, gp.tileSize * 2 + 45);
                String[] q4Opts = {"A. True", "B. False"};
                drawOptions(g2, q4Opts);
            } 
            else if (currentQuestion == 4) {
                g2.drawString("Q5: Most critical element in surviving any type of natural disaster is:", gp.tileSize * 1 + 30, gp.tileSize * 2 + 45);
                String[] q5Opts = {
                    "A. Location",
                    "B. Timing of disaster",
                    "C. Preparation",
                    "D. None of the above"
                };
                drawOptions(g2, q5Opts);
            }

            if (feedbackTimer > 0) {
                feedbackTimer--;
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                g2.setColor(Color.RED);
                g2.drawString(feedbackMessage, gp.tileSize * 1 + 30, gp.tileSize * 9 - 10);
            }

            g2.setFont(new Font("Arial", Font.ITALIC, 12));
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawString("Use [W / S] to navigate choices | Press [ENTER] to select", gp.tileSize * 1 + 30, gp.tileSize * 10 - 15);

        } else {
            g2.setFont(new Font("Arial", Font.BOLD, 26));
            g2.setColor(Color.green);
            g2.drawString("All Questions Answered Correctly!", gp.tileSize * 2, gp.tileSize * 5);
            g2.setFont(new Font("Arial", Font.PLAIN, 18));
            g2.setColor(Color.white);
            g2.drawString("You have mastered earthquake survival guidelines.", gp.tileSize * 2, gp.tileSize * 6);
            g2.drawString("Press [ENTER] to return to the game.", gp.tileSize * 2, gp.tileSize * 7);
        }
    }

    private void drawOptions(Graphics2D g2, String[] options) {
        int yPos = gp.tileSize * 3 + 20;
        for (int i = 0; i < options.length; i++) {
            if (earthquakeCursor == i) {
                g2.setColor(Color.yellow);
                g2.drawString("> " + options[i], gp.tileSize * 1 + 30, yPos);
            } else {
                g2.setColor(Color.white);
                g2.drawString("  " + options[i], gp.tileSize * 1 + 30, yPos);
            }
            yPos += 45;
        }
    }

    public void handlePuzzleInput(int choice) {

    // Don't allow the puzzle to be answered again
    if (earthquakeSolved || currentQuestion >= 5) {
        return;
    }

    boolean correct = false;

    if (currentQuestion == 0 && choice == 0) {
        correct = true;
    }
    else if (currentQuestion == 1 && choice == 0) {
        correct = true;
    }
    else if (currentQuestion == 2 && choice == 0) {
        correct = true;
    }
    else if (currentQuestion == 3 && choice == 0) {
        correct = true;
    }
    else if (currentQuestion == 4 && choice == 2) {
        correct = true;
    }

    if (correct) {

        currentQuestion++;
        earthquakeCursor = 0;
        feedbackTimer = 0;

        // Finished all 5 questions
        if (currentQuestion >= 5) {

            earthquakeSolved = true;
            gp.earthquakePuzzleSolved = true;

            // Return to normal gameplay
            gp.gameState = gp.playState;

        } else {

            puzzleTimer = maxPuzzleTime;
        }

    } else {

        failPuzzlePenalty();
    }
}
    public int currentGlyphNumber = 2; 
    public boolean[] glyphSolved = {false, false, false, false, false, false}; 
    public int glyphCursor = 0;
    public int glyphCurrentQuestion = 0;

    public void startGlyphChallenge(int glyphIndex) {
        currentGlyphNumber = glyphIndex;
        glyphCurrentQuestion = 0;
        glyphCursor = 0;
        puzzleTimer = maxPuzzleTime; 
        feedbackTimer = 0;
        currentSpeaker = "Oracle"; 
        gp.gameState = gp.puzzleState; 
    }

    public void drawGlyphPuzzle(Graphics2D g2) {
        if (!glyphSolved[currentGlyphNumber]) {
            puzzleTimer--;
            if (puzzleTimer <= 0) {
                failPuzzlePenalty(); 
                return;
            }
        }

        g2.setColor(new Color(0, 0, 0, 220));
        g2.fillRect(gp.tileSize * 1, gp.tileSize * 1, gp.tileSize * 14, gp.tileSize * 10);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(gp.tileSize * 1 + 10, gp.tileSize * 1 + 10, gp.tileSize * 14 - 20, gp.tileSize * 10 - 20);

        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("GLYPH CHALLENGE #" + currentGlyphNumber, gp.tileSize * 1 + 30, gp.tileSize * 2);

        int secondsLeft = puzzleTimer / 60;
        g2.setColor(secondsLeft <= 10 ? Color.RED : Color.YELLOW);
        g2.drawString("Time Left: " + secondsLeft + "s", gp.tileSize * 10 + 20, gp.tileSize * 2);

        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.PLAIN, 15));

        if (!glyphSolved[currentGlyphNumber]) {
            renderGlyphQuestions(g2);

            if (feedbackTimer > 0) {
                feedbackTimer--;
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                g2.setColor(Color.RED);
                g2.drawString(feedbackMessage, gp.tileSize * 1 + 30, gp.tileSize * 9 - 10);
            }

            g2.setFont(new Font("Arial", Font.ITALIC, 12));
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawString("Use [W / S] to navigate choices | Press [ENTER] to select", gp.tileSize * 1 + 30, gp.tileSize * 10 - 15);
        } else {
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.setColor(Color.green);
            g2.drawString("Glyph #" + currentGlyphNumber + " Cleared!", gp.tileSize * 2, gp.tileSize * 5);
            g2.setFont(new Font("Arial", Font.PLAIN, 18));
            g2.setColor(Color.white);
            g2.drawString("Press [ENTER] to return to the game.", gp.tileSize * 2, gp.tileSize * 6);
        }
    }

    private void renderGlyphQuestions(Graphics2D g2) {
        String questionText = "";
        String[] options = {};

        if (currentGlyphNumber == 2) {
            if (glyphCurrentQuestion == 0) {
                questionText = "Q1: Are elevators and escalators safe to use during an earthquake?";
                options = new String[]{"A. True", "B. False"};
            } else if (glyphCurrentQuestion == 1) {
                questionText = "Q2: Should you use the stairs instead of an elevator during an earthquake?";
                options = new String[]{"A. True", "B. False"};
            } else if (glyphCurrentQuestion == 2) {
                questionText = "Q3: Should you stay indoors if an earthquake is happening while you are inside a building?";
                options = new String[]{"A. True", "B. False"};
            } else if (glyphCurrentQuestion == 3) {
                questionText = "Q4: Is standing near a window during an earthquake safe?";
                options = new String[]{"A. True", "B. False"};
            }
        } 
        else if (currentGlyphNumber == 3) {
            if (glyphCurrentQuestion == 0) {
                questionText = "Q1: Is standing near a window during an earthquake safe?";
                options = new String[]{"A. True", "B. False"};
            } else if (glyphCurrentQuestion == 1) {
                questionText = "Q2: Should you Drop, Cover, and Hold On during an earthquake?";
                options = new String[]{"A. True", "B. False"};
            } else if (glyphCurrentQuestion == 2) {
                questionText = "Q3: Is it safe to run outside immediately while the ground is shaking?";
                options = new String[]{"A. True", "B. False"};
            } else if (glyphCurrentQuestion == 3) {
                questionText = "Q4: Should you stay away from shelves and objects that could fall during an earthquake?";
                options = new String[]{"A. True", "B. False"};
            } else if (glyphCurrentQuestion == 4) {
                questionText = "Q5: Should you protect your head and neck during an earthquake?";
                options = new String[]{"A. True", "B. False"};
            }
        }
        else if (currentGlyphNumber == 4) {
            if (glyphCurrentQuestion == 0) {
                questionText = "Q1: Is a doorway always the safest place to stand during an earthquake?";
                options = new String[]{"A. True", "B. False"};
            } else if (glyphCurrentQuestion == 1) {
                questionText = "Q2: Should you move away from power lines after an earthquake?";
                options = new String[]{"A. True", "B. False"};
            } else if (glyphCurrentQuestion == 2) {
                questionText = "Q3: What should you do if you are indoors when an earthquake starts?";
                options = new String[]{"A. Run outside immediately", "B. Drop, Cover, and Hold On", "C. Stand beside a window", "D. Use the elevator"};
            } else if (glyphCurrentQuestion == 3) {
                questionText = "Q4: What should you avoid during an earthquake?";
                options = new String[]{"A. Taking cover under a sturdy table", "B. Protecting your head", "C. Standing near windows", "D. Holding onto your shelter"};
            }
        }
        else if (currentGlyphNumber == 5) {
            if (glyphCurrentQuestion == 0) {
                questionText = "Q1: If you are in a classroom during an earthquake, what should you do?";
                options = new String[]{"A. Run toward the door", "B. Hide under a sturdy desk and hold on", "C. Stand near the windows", "D. Use the elevator"};
            } else if (glyphCurrentQuestion == 1) {
                questionText = "Q2: What should you do after the shaking stops?";
                options = new String[]{"A. Immediately return to normal activities", "B. Check yourself for injuries and follow safety instructions", "C. Use the elevator", "D. Stand near damaged buildings"};
            } else if (glyphCurrentQuestion == 2) {
                questionText = "Q3: If you are outside during an earthquake, where should you move?";
                options = new String[]{"A. Near buildings", "B. Under power lines", "C. To an open area away from buildings and other hazards", "D. Inside the nearest elevator"};
            } else if (glyphCurrentQuestion == 3) {
                questionText = "Q4: What should you do if you are driving when an earthquake occurs?";
                options = new String[]{"A. Speed up", "B. Stop in a safe location away from bridges and power lines", "C. Drive under a bridge", "D. Leave the car immediately in the middle of traffic"};
            } else if (glyphCurrentQuestion == 4) {
                questionText = "Q5: Should you expect aftershocks following a strong earthquake?";
                options = new String[]{"A. True", "B. False"};
            } else if (glyphCurrentQuestion == 5) {
                questionText = "Q6: Is it safe to enter a heavily damaged building after an earthquake?";
                options = new String[]{"A. True", "B. False"};
            }
        }

        int textX = gp.tileSize * 1 + 30;
        int textY = gp.tileSize * 2 + 45;
        int maxTextWidth = (gp.tileSize * 14) - 60;
        
        drawWrappedString(g2, questionText, textX, textY, maxTextWidth, 20);
        
        int yPos = gp.tileSize * 4 + 10;
        for (int i = 0; i < options.length; i++) {
            if (glyphCursor == i) {
                g2.setColor(Color.yellow);
                g2.drawString("> " + options[i], textX, yPos);
            } else {
                g2.setColor(Color.white);
                g2.drawString("  " + options[i], textX, yPos);
            }
            yPos += 35;
        }
    }

    public void handleInput(int keyCode) {
        if (gp.gameState == gp.puzzleState) {
            int maxChoiceLimit = 3; 
            
            boolean isGlyphMode = (gp.tileM.currentMap == 6 && currentGlyphNumber >= 2 && currentGlyphNumber <= 5 && !glyphSolved[currentGlyphNumber]);
            boolean isOracleMode = (gp.tileM.currentMap == 8); // Added Oracle check

            if (isOracleMode) {
                maxChoiceLimit = oracleOptions[oracleCurrentQuestion].length - 1;
            }
            else if (isGlyphMode) {
                if (currentGlyphNumber == 2 && (glyphCurrentQuestion >= 0 && glyphCurrentQuestion <= 3)) maxChoiceLimit = 1;
                else if (currentGlyphNumber == 3 && (glyphCurrentQuestion >= 0 && glyphCurrentQuestion <= 4)) maxChoiceLimit = 1;
                else if (currentGlyphNumber == 4 && (glyphCurrentQuestion == 0 || glyphCurrentQuestion == 1)) maxChoiceLimit = 1;
                else if (currentGlyphNumber == 5 && (glyphCurrentQuestion == 4 || glyphCurrentQuestion == 5)) maxChoiceLimit = 1;
            } else {
                if (currentQuestion == 2 || currentQuestion == 3) maxChoiceLimit = 1;
                else maxChoiceLimit = 3;
            }

            if (keyCode == java.awt.event.KeyEvent.VK_W || keyCode == java.awt.event.KeyEvent.VK_UP) {
                if (isOracleMode) {
                    oracleCursor--;
                    if (oracleCursor < 0) oracleCursor = maxChoiceLimit;
                }
                else if (isGlyphMode) {
                    glyphCursor--;
                    if (glyphCursor < 0) glyphCursor = 0;
                } else {
                    earthquakeCursor--;
                    if (earthquakeCursor < 0) earthquakeCursor = 0;
                }
            } 
            else if (keyCode == java.awt.event.KeyEvent.VK_S || keyCode == java.awt.event.KeyEvent.VK_DOWN) {
                if (isOracleMode) {
                    oracleCursor++;
                    if (oracleCursor > maxChoiceLimit) oracleCursor = 0;
                }
                else if (isGlyphMode) {
                    glyphCursor++;
                    if (glyphCursor > maxChoiceLimit) glyphCursor = maxChoiceLimit;
                } else {
                    earthquakeCursor++;
                    if (earthquakeCursor > maxChoiceLimit) earthquakeCursor = maxChoiceLimit;
                }
            }
            else if (keyCode == java.awt.event.KeyEvent.VK_ENTER) {
                if (isOracleMode) {
                    handleOracleInput(oracleCursor);
                }
                else if (isGlyphMode) {
                    if (glyphSolved[currentGlyphNumber]) {
                        gp.gameState = gp.playState;
                        return;
                    }
                    handleGlyphInput(glyphCursor);
                } else {
                    if (earthquakeSolved || currentQuestion >= 5) {
                        earthquakeSolved = true;
                        gp.gameState = gp.playState;
                        return;
                    }
                    handlePuzzleInput(earthquakeCursor);
                }
            }
        }
    }

    public void handleGlyphInput(int choice) {
        if (glyphSolved[currentGlyphNumber]) {
            gp.gameState = gp.playState;
            return;
        }

        boolean correct = false;
        int maxQuestions = 4; 

        if (currentGlyphNumber == 2) {
            maxQuestions = 4;
            if (glyphCurrentQuestion == 0 && choice == 1) correct = true; 
            else if (glyphCurrentQuestion == 1 && choice == 0) correct = true; 
            else if (glyphCurrentQuestion == 2 && choice == 0) correct = true; 
            else if (glyphCurrentQuestion == 3 && choice == 1) correct = true; 
        } 
        else if (currentGlyphNumber == 3) {
            maxQuestions = 5;
            if (glyphCurrentQuestion == 0 && choice == 1) correct = true; 
            else if (glyphCurrentQuestion == 1 && choice == 0) correct = true; 
            else if (glyphCurrentQuestion == 2 && choice == 1) correct = true; 
            else if (glyphCurrentQuestion == 3 && choice == 0) correct = true; 
            else if (glyphCurrentQuestion == 4 && choice == 0) correct = true; 
        }
        else if (currentGlyphNumber == 4) {
            maxQuestions = 4;
            if (glyphCurrentQuestion == 0 && choice == 1) correct = true; 
            else if (glyphCurrentQuestion == 1 && choice == 0) correct = true; 
            else if (glyphCurrentQuestion == 2 && choice == 1) correct = true; 
            else if (glyphCurrentQuestion == 3 && choice == 2) correct = true; 
        }
        else if (currentGlyphNumber == 5) {
            maxQuestions = 6;
            if (glyphCurrentQuestion == 0 && choice == 1) correct = true; 
            else if (glyphCurrentQuestion == 1 && choice == 1) correct = true; 
            else if (glyphCurrentQuestion == 2 && choice == 2) correct = true; 
            else if (glyphCurrentQuestion == 3 && choice == 1) correct = true; 
            else if (glyphCurrentQuestion == 4 && choice == 0) correct = true; 
            else if (glyphCurrentQuestion == 5 && choice == 1) correct = true; 
        }

        if (correct) {
            glyphCurrentQuestion++;
            glyphCursor = 0;
            feedbackTimer = 0;
            
            if (glyphCurrentQuestion >= maxQuestions) {
                glyphSolved[currentGlyphNumber] = true;
                
                // Check if glyphs 2, 3, 4, and 5 are ALL solved now
                boolean allTopSolved = glyphSolved[2] && glyphSolved[3] && glyphSolved[4] && glyphSolved[5];
                
                if (allTopSolved) {
                    String validSpeaker = (playerName != null && !playerName.trim().isEmpty()) ? playerName.trim() : "Lumberjack";
                    String[] pathOpenDialogues = {
                        "Glyph #" + currentGlyphNumber + " cleared! All corner glyphs are now active.",
                        "A deep rumbling echoes... The right path has opened!"
                    };
                    startNPCDialogue(validSpeaker, pathOpenDialogues);
                } else {
                    gp.gameState = gp.playState; 
                }
            } else {
                puzzleTimer = maxPuzzleTime; 
            }
        } else {
            if (!glyphSolved[currentGlyphNumber]) {
                puzzleTimer -= 300;
                feedbackMessage = "Incorrect! -5 Seconds";
                feedbackTimer = 60; 

                if (puzzleTimer <= 0) {
                    failPuzzlePenalty();
                }
            }
        }
    }

    private void failPuzzlePenalty() {
        if (earthquakeSolved && currentGlyphNumber == 1) return;
        if (currentGlyphNumber >= 2 && currentGlyphNumber <= 5 && glyphSolved[currentGlyphNumber]) return;

        isFailureSequenceActive = true;
        gp.triggerScreenShake(300);

        String validSpeaker = (playerName != null && !playerName.trim().isEmpty()) ? playerName.trim() : "Lumberjack";
        String[] failDialogues = {
            "Oh, what's happening?! The ground is shaking violently!",
            "I failed to prepare properly... I need to get back to safety!"
        };

        startNPCDialogue(validSpeaker, failDialogues);
    }

    private void executeFailTeleport() {
        gp.screenShakeCounter = 0; 
        gp.tileM.currentMap = 3;
        gp.player.x = gp.tileSize * 8;
        gp.player.y = gp.tileSize * 3;
        gp.gameState = gp.playState;
    }

    public void resetGlyphPuzzle(int glyphNumber) {
        currentGlyphNumber = glyphNumber;
        glyphCurrentQuestion = 0;
        glyphCursor = 0;
        feedbackTimer = 0;
    }

    public void drawCutscene(Graphics2D g2) {}
    public void updateCutscene() {}

    // --- Oracle Map 8 Methods ---
    public void drawOracleQuestion(Graphics2D g2) {
        if (isFailingOracle) {
            handleOracleFailSequence();
            return;
        }

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 18)); 
        
        String question = "Oracle: " + oracleQuestions[oracleCurrentQuestion];
        int x = gp.tileSize / 2;
        int y = gp.tileSize * 2;
        g2.drawString(question, x, y);

        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        for(int i = 0; i < oracleOptions[oracleCurrentQuestion].length; i++) {
            if (oracleCursor == i) {
                g2.setColor(Color.YELLOW);
                g2.drawString("> " + oracleOptions[oracleCurrentQuestion][i], x + 20, y + gp.tileSize * (2 + i));
            } else {
                g2.setColor(Color.WHITE);
                g2.drawString("  " + oracleOptions[oracleCurrentQuestion][i], x + 20, y + gp.tileSize * (2 + i));
            }
        }
        
        g2.setFont(new Font("Arial", Font.ITALIC, 16));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("[W / S] to move, [ENTER] to confirm", x, y + gp.tileSize * 7);
    }

    public void handleOracleInput(int choice) {
        if (choice == oracleCorrectAnswers[oracleCurrentQuestion]) {
            oracleCurrentQuestion++;
            oracleCursor = 0; 
            
            // Check if all 8 questions are finished
            if (oracleCurrentQuestion >= oracleQuestions.length) {
                // Set the flag to true so the game knows you won
                gameCompleted = true; 
                
                // Open the dialogue box for the cinematic ending
                gp.gameState = gp.dialogueState;
                String[] dramaticWinMsg = {
                    "Oracle: You have proven your readiness...",
                    "Oracle: You have escaped this challenge... for now.",
                    "Oracle: The storm clouds break, parting the heavens above.",
                    "Oracle: But remember... the forest never forgets."
                };
                startNPCDialogue("Oracle", dramaticWinMsg);
            }
        } else {
            isFailingOracle = true;
            shakeFailTimer = 0; 
        }
    }

    // Keep track of which question failed so we know where to teleport after the shake
    private int failedAtQuestion = 0;

    public void handleOracleFailSequence() {
        shakeFailTimer++; 
        
        // When the shake first starts (at tick 1), save the current question index
        if (shakeFailTimer == 1) {
            failedAtQuestion = oracleCurrentQuestion;
            gp.triggerScreenShake(30);
        } 
        else if (shakeFailTimer == 60) gp.triggerScreenShake(70); 
        else if (shakeFailTimer == 120) gp.triggerScreenShake(150); 
        else if (shakeFailTimer == 180) { 
            
            isFailingOracle = false;
            oracleCursor = 0; 
            oracleCurrentQuestion = 0; 
            
            String validSpeaker = (playerName != null && !playerName.trim().isEmpty()) ? playerName.trim() : "Lumberjack";
            String[] failMsg;
            
            // Check if the failure happened on the 8th question (index 7)
            if (failedAtQuestion == 7) {
                // Send back to Map 3 with the custom deforestation dialogue
                gp.tileM.currentMap = 3; 
                gp.player.x = gp.tileSize * 8; 
                gp.player.y = gp.tileSize * 3; 
                gp.gameState = gp.playState;
                
                failMsg = new String[] {
                    "Oracle: Then why did you cut those trees?",
                    "Oracle: Ha! Try again!",
                    "The typhoon winds blew you back to Map 3..."
                };
            } else {
                // Send back to Map 7 (your existing earthquake failure sequence)
                gp.tileM.currentMap = 7; 
                gp.player.x = gp.tileSize * 5; 
                gp.player.y = gp.tileSize * 5; 
                gp.gameState = gp.playState;
                
                failMsg = new String[] {
                    "The typhoon winds were too strong!", 
                    "Your lack of preparation blew you back to Map 7..."
                };
            }
            
            startNPCDialogue(validSpeaker, failMsg);
        }
    }
    public void drawCredits(Graphics2D g2) {
        creditTimer++;

        // Black screen background for dramatic effect
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Calculate scrolling Y position
        int startY = gp.screenHeight - (creditTimer / 2);

        // Helper FontMetrics for centering text horizontally
        FontMetrics fm;

        // 1. Title
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("GothicByte", Font.BOLD, 30));
        String title = "DISASTER PREPAREDNESS: THE ODYSSEY";
        fm = g2.getFontMetrics();
        int titleX = (gp.screenWidth - fm.stringWidth(title)) / 2;
        g2.drawString(title, titleX, startY);

        // 2. Credits Body Text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("GothicByte", Font.PLAIN, 18));
        fm = g2.getFontMetrics();

        String line1 = "Created by: Lead Developer & Hero";
        String line2 = "Special Thanks to: The Oracle of the Storm";
        String line3 = "The Forest: Consumed for Timber";

        g2.drawString(line1, (gp.screenWidth - fm.stringWidth(line1)) / 2, startY + 100);
        g2.drawString(line2, (gp.screenWidth - fm.stringWidth(line2)) / 2, startY + 160);
        g2.drawString(line3, (gp.screenWidth - fm.stringWidth(line3)) / 2, startY + 220);

        // 3. Dramatic Twist Line
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("GothicByte", Font.ITALIC, 20));
        fm = g2.getFontMetrics();
        String twist = "...Or was it all just a dream?";
        g2.drawString(twist, (gp.screenWidth - fm.stringWidth(twist)) / 2, startY + 320);

        
        
        // Loop credits if they scroll all the way off the top
        if (startY < -400) {
            creditTimer = -gp.screenHeight * 2; 
        }
    }
}