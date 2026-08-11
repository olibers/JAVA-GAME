import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    public boolean villageOracleAppeared = false;
    public boolean map6OracleAppeared = false;
    public boolean guideBookReadyToClose = false;

    // MAP 8 ORACLE
    public boolean map8OracleInteracted = false;
    public boolean map8OracleAppeared = false;
    boolean map6IntroTriggered = false;
    

    public int treesChopped = 0;
    public final int MAX_TREES_TO_CHOP = 3;

    public boolean oracleAppeared = false;
    public boolean hasBeenToDarkPlace = false;
    public boolean oracleDisappeared = false;
    public boolean bookDropped = false;
    public boolean possessesGuideBook = false;
    public boolean villagePathUnlocked = false;
    public boolean earthquakePuzzleSolved = false;
    public boolean guideIntroShown = false;

    public boolean map7EntranceSealed = false;
    public boolean map8DialogueShown = false;

    public dyingcharacter dyingNpc = new dyingcharacter(this);

    // =====================================================
    // WIND PARTICLES
    // =====================================================

    private class WindParticle {

        int x, y, speed, length;

        WindParticle(
            int x,
            int y,
            int speed,
            int length
        ) {

            this.x = x;
            this.y = y;
            this.speed = speed;
            this.length = length;
        }
    }

    private WindParticle[] windParticles =
        new WindParticle[40];

    private Random particleRandom =
        new Random();

    // =====================================================
    // ORACLE
    // =====================================================

    public boolean isOracleDialogue = false;

    public String[] oracleMap5Dialogue = {

        "The right pathway unlocks as the inner sanctum's glyphs glow brightly!",

        "You proceed forward...",

        "Oracle: Ah, you have stepped into this final path...",

        "Oracle: I must admit, I am quite impressed by your learning and resilience.",

        "Oracle: But do not celebrate just yet—my deeds and trials for you are far from over."
    };

    // =====================================================
    // MAP 5 FLOOD
    // =====================================================

    public boolean map5FloodTriggered = false;
    public boolean map5FloodRunning = false;

    public int map5FloodCol = 0;
    public int map5FloodTimer = 0;

    // =====================================================
    // SCREEN SHAKE
    // =====================================================

    public int screenShakeCounter = 0;

    private Random shakeRandom =
        new Random();

    // =====================================================
    // OTHER VARIABLES
    // =====================================================

    public boolean map7EventDone = false;

    private boolean namePromptShown = false;

    public boolean barrier1Cleared = false;
    public boolean barrier2Cleared = false;
    public boolean barrier3Cleared = false;
    public boolean barrier4Cleared = false;
    public boolean barrier5Cleared = false;

    public boolean waitingForAnswer = false;
    public int activeQuestion = 0;

    public boolean waveAnimationRunning = false;
    public int waveAnimCounter = 0;
    public int targetWaveCol = 10;

    // =====================================================
    // SCREEN SETTINGS
    // =====================================================

    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize =
        originalTileSize * scale;

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;

    public final int screenWidth =
        tileSize * maxScreenCol;

    public final int screenHeight =
        tileSize * maxScreenRow;

    // =====================================================
    // GAME OBJECTS
    // =====================================================

    public TileManager tileM =
        new TileManager(this);

    public KeyHandler keyH =
        new KeyHandler(this);

    Thread gameThread;

    public CollisionChecker cChecker =
        new CollisionChecker(this);

    public UI ui =
        new UI(this);

    public Player player =
        new Player(this, keyH);

    public NPC oracle =
        new NPC(this);

    public BookItem guideBook =
        new BookItem(this, 8, 6);

    // =====================================================
    // GAME STATES
    // =====================================================

    public int gameState;

    public final int titleState = 0;
    public final int playState = 1;
    public final int cutsceneState = 2;
    public final int dialogueState = 3;
    public final int puzzleState = 4;
    public final int creditsState = 5; // (Use whatever number comes next in your sequence)

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public GamePanel() {

        this.setPreferredSize(
            new Dimension(
                screenWidth,
                screenHeight
            )
        );

        this.setBackground(Color.black);

        this.setDoubleBuffered(true);

        this.addKeyListener(keyH);

        this.setFocusable(true);

        this.requestFocusInWindow();

        // Initialize wind animation particles
        for (
            int i = 0;
            i < windParticles.length;
            i++
        ) {

            windParticles[i] =
                new WindParticle(
                    particleRandom.nextInt(screenWidth),
                    particleRandom.nextInt(screenHeight),
                    particleRandom.nextInt(8) + 10,
                    particleRandom.nextInt(20) + 15
                );
        }

        setupGame();
    }

    // =====================================================
    // SETUP GAME
    // =====================================================

    public void setupGame() {

        gameState = titleState;

        oracle.name = "Oracle";

        oracle.x =
            tileSize * 8;

        oracle.y =
            tileSize * 4;

        oracle.dialogues = new String[] {

            "WHY ARE YOU CHOPPING DOWN THESE TREES WITHOUT THOUGHT?!",

            "Do you even realize the severe consequences of what you have done?",

            "Your reckless deforestation destroys the natural barrier of this land!",

            "Because of your actions, sequential disasters are heading straight towards your home...",

            "First, an EARTHQUAKE will shatter the weakened soil.",

            "Then, a violent TYPHOON will sweep through the bare lands.",

            "Finally, catastrophic FLOODS will submerge everything in sight!",

            "Learn how to survive what you have caused... before it is too late!"
        };
    }

    // =====================================================
    // SCREEN SHAKE
    // =====================================================

    public void triggerScreenShake(int durationFrames) {

        this.screenShakeCounter =
            durationFrames;
    }

    // =====================================================
    // OPEN VILLAGE PATH
    // =====================================================

    public void openVillagePath() {

    if (!villagePathUnlocked) {

        villagePathUnlocked = true;

        tileM.mapTileNum[0][7][0] = 0;
        tileM.mapTileNum[0][8][0] = 0;
        tileM.mapTileNum[0][9][0] = 0;

        String[] pathMsg = {

            "As you finish reading the guide, a heavy rumbling echoes through the forest!",

            "The trees blocking the northern path have cleared!",

            "Follow the long road north to reach your house"
        };

        ui.startNPCDialogue(
            "System",
            pathMsg
        );
    }
}

    // =====================================================
    // RESET MAP 5
    // =====================================================

    public void resetMap5Progress() {

        barrier1Cleared = false;
        barrier2Cleared = false;
        barrier3Cleared = false;

        waitingForAnswer = false;

        for (
            int c = 43;
            c <= 46;
            c++
        ) {

            if (
                c <
                tileM.mapTileNum[5].length
            ) {

                tileM.mapTileNum[5][c][maxScreenRow - 1] = 0;
            }
        }
    }

    // =====================================================
    // OPEN MAP 5 EXIT
    // =====================================================

    public void openMap5ExitPath() {

        for (
            int c = 43;
            c <= 46;
            c++
        ) {

            if (
                c <
                tileM.mapTileNum[5].length
            ) {

                tileM.mapTileNum[5][c][maxScreenRow - 1] = 0;
            }
        }
    }

    // =====================================================
    // START GAME THREAD
    // =====================================================

    public void startGameThread() {

        gameThread =
            new Thread(this);

        gameThread.start();
    }

    // =====================================================
    // GAME LOOP
    // =====================================================

    @Override
    public void run() {

        double drawInterval =
            1000000000 / 60;

        double delta = 0;

        long lastTime =
            System.nanoTime();

        while (gameThread != null) {

            long currentTime =
                System.nanoTime();

            delta +=
                (currentTime - lastTime)
                / drawInterval;

            lastTime =
                currentTime;

            if (delta >= 1) {

                update();

                repaint();

                delta--;
            }
        }
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public void update() {

        // =================================================
        // MAP 8 WIND PARTICLES
        // =================================================

        if (tileM.currentMap == 8) {

            for (
                int i = 0;
                i < windParticles.length;
                i++
            ) {

                windParticles[i].x -=
                    windParticles[i].speed;

                if (
                    windParticles[i].x < 0
                ) {

                    windParticles[i].x =
                        screenWidth;

                    windParticles[i].y =
                        particleRandom.nextInt(
                            screenHeight
                        );
                }
            }
        }

        // =================================================
        // PLAYER SPEED
        // =================================================

        if (tileM.currentMap == 8) {

            player.speed =
                player.defaultSpeed / 2;

        } else {

            player.speed =
                player.defaultSpeed;
        }

        // =================================================
        // MAP 5 ORACLE FLOOD
        // =================================================

        if (
            isOracleDialogue &&
            gameState == playState &&
            !map5FloodTriggered
        ) {

            map5FloodTriggered = true;

            map5FloodRunning = true;

            map5FloodCol =
                tileM.mapTileNum[5].length - 2;

            triggerScreenShake(100);

            tileM.mapTileNum[5][0][9] = 1;
            tileM.mapTileNum[5][0][10] = 1;
        }

        // =================================================
        // MAP 5 FLOOD ANIMATION
        // =================================================

        if (
            map5FloodRunning &&
            tileM.currentMap == 5
        ) {

            map5FloodTimer++;

            if (map5FloodTimer > 2) {

                map5FloodTimer = 0;

                if (map5FloodCol >= 1) {

                    for (
                        int r = 1;
                        r <
                        tileM.mapTileNum[5]
                        [map5FloodCol].length - 1;
                        r++
                    ) {

                        tileM.mapTileNum[5]
                            [map5FloodCol][r] = 20;
                    }

                    map5FloodCol--;

                } else {

                    map5FloodRunning = false;
                }
            }
        }

        // =================================================
        // SCREEN SHAKE
        // =================================================

        if (screenShakeCounter > 0) {

            screenShakeCounter--;
        }

        // =================================================
        // MAP 5 RETREAT
        // =================================================

        if (
            tileM.currentMap == 5 &&
            player.x < 0
        ) {

            resetMap5Progress();

            tileM.currentMap = 3;

            player.x =
                tileSize *
                (maxScreenCol - 2);
        }

        // =================================================
        // MAP 3 / MAP 4
        // =================================================

        if (
            tileM.currentMap == 3 &&
            player.x < 0
        ) {

            tileM.currentMap = 4;

            player.x =
                screenWidth -
                (tileSize * 2);

            player.y =
                tileSize * 9;
        }

        else if (
            tileM.currentMap == 4 &&
            player.x >=
            screenWidth - tileSize
        ) {

            tileM.currentMap = 3;

            player.x =
                tileSize * 2;

            player.y =
                tileSize * 9;
        }

        else if (
            tileM.currentMap == 3 &&
            player.y <= 0
        ) {

            int col =
                player.x / tileSize;

            if (
                col >= 7 &&
                col <= 8
            ) {

                if (earthquakePuzzleSolved) {

                    tileM.currentMap = 6;

                    player.y =
                        tileSize *
                        (maxScreenRow - 2);

                } else {

                    player.y =
                        tileSize;

                    String[] lockedMsg = {

                        "The way north is sealed by a mysterious barrier!",

                        "You must solve the Earthquake Glyph puzzle on the left first."
                    };

                    ui.startNPCDialogue(
                        "System",
                        lockedMsg
                    );
                }

            } else {

                player.y =
                    tileSize;
            }
        }

        // =================================================
        // MAP 6 RETURN
        // =================================================

        else if (
            tileM.currentMap == 6 &&
            player.y >= screenHeight
        ) {

            tileM.currentMap = 3;

            player.y =
                tileSize;
        }

        // =================================================
        // MAP 7
        // =================================================

        else if (
            tileM.currentMap == 7
        ) {

            dyingNpc.x =
                tileSize * 8;

            dyingNpc.y =
                tileSize * 6;

            int dx =
                Math.abs(
                    player.x -
                    dyingNpc.x
                );

            int dy =
                Math.abs(
                    player.y -
                    dyingNpc.y
                );

            // Talk to dying traveler
            if (
                dx < tileSize * 1.5 &&
                dy < tileSize * 1.5 &&
                keyH.enterPressed
            ) {

                keyH.enterPressed = false;

                ui.startNPCDialogue(
                    "Dying Traveler",
                    dyingNpc.dialogues
                );
            }

            // After dialogue, open Map 7 right exit
            if (dyingNpc.dialogueFinished) {

                for (
                    int r = 4;
                    r <= 6;
                    r++
                ) {

                    tileM.mapTileNum[7]
                        [maxScreenCol - 1][r] = 0;
                }

                // Enter Map 8
                if (
                    player.x >=
                    screenWidth - tileSize
                ) {

                    tileM.currentMap = 8;

                    player.x =
                        tileSize * 2;

                    player.y =
                        tileSize * 5;

                    // Reset Map 8 Oracle interaction
                    map8OracleInteracted = false;
                    map8OracleAppeared = false;

                    String[] map8Msg = {

                        "You stepped through into Map 8..."
                    };

                    ui.startNPCDialogue(
                        "System",
                        map8Msg
                    );
                }
            }
        }

        // =================================================
        // MAP 8
        // =================================================

        else if (
            tileM.currentMap == 8
        ) {

            // ---------------------------------------------
            // SMALL ORACLE POSITION
            // ---------------------------------------------

            oracle.x =
                tileSize * 8;

            oracle.y =
                tileSize * 5;

            // ---------------------------------------------
            // INTERACT WITH SMALL ORACLE
            // ---------------------------------------------

            if (!map8OracleInteracted) {

                int dx =
                    Math.abs(
                        player.x -
                        oracle.x
                    );

                int dy =
                    Math.abs(
                        player.y -
                        oracle.y
                    );

                if (
                    dx < tileSize * 1.5 &&
                    dy < tileSize * 1.5 &&
                    keyH.enterPressed
                ) {

                    keyH.enterPressed = false;

                    // Small Oracle has been interacted with
                    map8OracleInteracted = true;

                    // Big Oracle now appears
                    map8OracleAppeared = true;

                    String[] map8OracleMsg = {

                        "Oracle: You have finally arrived...",

                        "The darkness you see is only the beginning.",

                        "A terrible typhoon is approaching.",

                        "You must prove that you know how to survive it."
                    };

                    ui.startNPCDialogue(
                        "Oracle",
                        map8OracleMsg
                    );
                }
            }

            // ---------------------------------------------
            // RETURN TO MAP 7
            // ---------------------------------------------

            if (player.x <= 0) {

                tileM.currentMap = 7;

                player.x =
                    screenWidth -
                    (tileSize * 2);

                player.y =
                    tileSize * 5;
            }
        }

        // =================================================
        // GAME STATE
        // =================================================

        if (gameState == titleState) {

            if (!namePromptShown) {

                namePromptShown = true;

                ui.chooseName();
            }
        }

        // =================================================
        // PLAY STATE
        // =================================================

        else if (
            gameState == playState
        ) {

            // Player movement
            if (!keyH.bookPressed) {

                player.update();
            }

            // =================================================
            // GUIDE BOOK
            // =================================================

            if (
                possessesGuideBook &&
                keyH.bookPressed &&
                !villagePathUnlocked
            ) {

                openVillagePath();
            }

            // =================================================
            // MAP 0
            // =================================================

            if (
                tileM.currentMap == 0
            ) {

                // Go to long road
                if (
                    villagePathUnlocked &&
                    player.y <= 0
                ) {

                    tileM.currentMap = 2;

                    player.x =
                        tileSize * 8;

                    player.y =
                        screenHeight -
                        (tileSize * 2);

                    String[] roadMsg = {

                        "You stepped onto the Long Road leading to your house."
                    };

                    ui.startNPCDialogue(
                        "System",
                        roadMsg
                    );
                }

                // Chop trees
                if (
                    keyH.enterPressed &&
                    treesChopped < MAX_TREES_TO_CHOP &&
                    !hasBeenToDarkPlace
                ) {

                    keyH.enterPressed = false;

                    int checkX =
                        player.x +
                        player.solidArea.x +
                        (player.solidArea.width / 2);

                    int checkY =
                        player.y +
                        player.solidArea.y +
                        (player.solidArea.height / 2);

                    switch (player.direction) {

                        case "up":
                            checkY -= tileSize;
                            break;

                        case "down":
                            checkY += tileSize;
                            break;

                        case "left":
                            checkX -= tileSize;
                            break;

                        case "right":
                            checkX += tileSize;
                            break;
                    }

                    int col =
                        checkX / tileSize;

                    int row =
                        checkY / tileSize;

                    if (
                        col >= 0 &&
                        col < maxScreenCol &&
                        row >= 0 &&
                        row < maxScreenRow
                    ) {

                        if (
                            tileM.mapTileNum[0]
                                [col][row] == 1
                        ) {

                            tileM.mapTileNum[0]
                                [col][row] = 0;

                            treesChopped++;

                            if (
                                treesChopped <
                                MAX_TREES_TO_CHOP
                            ) {

                                String[] chopMsg = {

                                    "*Chop! Chop!* You chopped down a tree.",

                                    "Trees Chopped: "
                                    + treesChopped
                                    + "/"
                                    + MAX_TREES_TO_CHOP
                                };

                                ui.startNPCDialogue(
                                    ui.playerName,
                                    chopMsg
                                );

                            } else {

                                oracleAppeared = true;

                                oracle.x =
                                    player.x;

                                oracle.y =
                                    player.y -
                                    tileSize;

                                String[] frightenedMsg = {

                                    "*Chop! Chop!* The 3rd tree falls down.",

                                    "Phew! That's another tree down...",

                                    "Wait... why did the air suddenly turn freezing cold?",

                                    "A strange man (Oracle) suddenly appears right behind your back, glaring at you angrily!",

                                    ui.playerName +
                                    " is terrified!",

                                    "The Oracle casts a spell and teleports you both away!"
                                };

                                ui.startNPCDialogue(
                                    "System",
                                    frightenedMsg
                                );
                            }
                        }
                    }
                }

                // Teleport to Dark Place
                if (
                    oracleAppeared &&
                    !hasBeenToDarkPlace &&
                    gameState != dialogueState
                ) {

                    hasBeenToDarkPlace = true;

                    tileM.currentMap = 1;

                    player.x =
                        tileSize * 8;

                    player.y =
                        tileSize * 8;

                    oracle.x =
                        tileSize * 8;

                    oracle.y =
                        tileSize * 4;

                    String[] darkPlaceMsg = {

                        "You were teleported into a dark, shadowy void!",

                        "Approach the strange man (Oracle) and press [E] to speak."
                    };

                    ui.startNPCDialogue(
                        "System",
                        darkPlaceMsg
                    );
                }
            }

            // =================================================
            // MAP 1
            // =================================================

            else if (
                tileM.currentMap == 1
            ) {

                int dx =
                    Math.abs(
                        player.x -
                        oracle.x
                    );

                int dy =
                    Math.abs(
                        player.y -
                        oracle.y
                    );

                if (
                    dx < tileSize * 2 &&
                    dy < tileSize * 2 &&
                    keyH.enterPressed &&
                    !bookDropped &&
                    !oracleDisappeared
                ) {

                    keyH.enterPressed = false;

                    ui.startNPCDialogue(
                        oracle.name,
                        oracle.dialogues
                    );

                    bookDropped = true;
                }

                if (
                    bookDropped &&
                    gameState != dialogueState &&
                    !oracleDisappeared
                ) {

                    oracleDisappeared = true;

                    guideBook.x =
                        tileSize * 8;

                    guideBook.y =
                        tileSize * 4;

                    guideBook.visible =
                        true;

                    String[] dropMsg = {

                        "The Oracle suddenly vanishes into thin air!",

                        "A glowing [Disaster Preparedness Guide Book] falls to the floor."
                    };

                    ui.startNPCDialogue(
                        "System",
                        dropMsg
                    );
                }

                if (guideBook.visible) {

                    int bdx =
                        Math.abs(
                            player.x -
                            guideBook.x
                        );

                    int bdy =
                        Math.abs(
                            player.y -
                            guideBook.y
                        );

                    if (
    bdx < tileSize * 1.5 &&
    bdy < tileSize * 1.5 &&
    keyH.enterPressed
) {

    keyH.enterPressed = false;

    guideBook.visible = false;

    possessesGuideBook = true;

    // Keep the guide book CLOSED
    keyH.bookPressed = false;

    tileM.currentMap = 0;

    player.x = tileSize * 7;
    player.y = tileSize * 5;

    String[] returnMsg = {

        "You picked up the [Disaster Preparedness Guide Book]!",

        "A blinding flash of light surrounds you...",

        "You were teleported back to the forest!",

        "Press [B] to open and read your Disaster Preparedness Guide!"
    };

    ui.startNPCDialogue(
        "System",
        returnMsg
    );
}
                }
            }

            // =================================================
            // MAP 2
            // =================================================

            else if (
                tileM.currentMap == 2
            ) {

                if (
                    player.y <= 0
                ) {

                    tileM.currentMap = 3;

                    player.x =
                        tileSize * 8;

                    player.y =
                        screenHeight -
                        (tileSize * 2);

                    String[] villageMsg = {

                        "You have arrived at your house!"
                    };

                    ui.startNPCDialogue(
                        "System",
                        villageMsg
                    );

                }

                else if (
                    player.y >=
                    screenHeight - tileSize
                ) {

                    tileM.currentMap = 0;

                    player.x =
                        tileSize * 8;

                    player.y =
                        tileSize * 2;
                }
            }

            // =================================================
            // MAP 3
            // =================================================

            else if (
                tileM.currentMap == 3
            ) {

                // NO ORACLE DRAWING OR EVENT HERE

                if (
                    player.y >=
                    screenHeight - tileSize
                ) {

                    tileM.currentMap = 2;

                    player.x =
                        tileSize * 8;

                    player.y =
                        tileSize * 2;
                }

                if (
                    player.x <= 5
                ) {

                    tileM.currentMap = 4;

                    player.x =
                        screenWidth -
                        (tileSize * 2);

                    player.y =
                        tileSize * 9;
                }

                if (
                    player.x >=
                    screenWidth - tileSize
                ) {

                    boolean allGlyphsDone =
                        ui.glyphSolved[2] &&
                        ui.glyphSolved[3] &&
                        ui.glyphSolved[4] &&
                        ui.glyphSolved[5];

                    if (allGlyphsDone) {

                        tileM.currentMap = 5;

                        player.x =
                            tileSize * 2;

                        openMap5ExitPath();

                        isOracleDialogue = true;

                        ui.startNPCDialogue(
                            "Oracle",
                            oracleMap5Dialogue
                        );

                    } else {

                        player.x =
                            screenWidth -
                            tileSize -
                            10;

                        String[] lockedRightMsg = {

                            "The right path is sealed by a powerful barrier!",

                            "You must complete all four corner glyph challenges in the northern room first."
                        };

                        ui.startNPCDialogue(
                            "System",
                            lockedRightMsg
                        );
                    }
                }
            }

            // =================================================
            // MAP 4
            // =================================================

            else if (
                tileM.currentMap == 4
            ) {

                if (
                    player.x >=
                    screenWidth - tileSize
                ) {

                    tileM.currentMap = 3;

                    player.x =
                        tileSize * 2;

                    player.y =
                        tileSize * 9;
                }

                int glyphX =
                    tileSize * 4;

                int glyphY =
                    tileSize * 6;

                int distance =
                    Math.abs(
                        player.x - glyphX
                    )
                    +
                    Math.abs(
                        player.y - glyphY
                    );

                if (
                    distance < tileSize &&
                    keyH.enterPressed
                ) {

                    keyH.enterPressed = false;

                    gameState =
                        puzzleState;

                    ui.resetEarthquakePuzzle();
                }
            }

            // =================================================
            // MAP 6
            // =================================================

            else if (
                tileM.currentMap == 6
            ) {


                if (!map6IntroTriggered) {
                map6IntroTriggered = true;
                
                String[] oracleMap6Dialogue = {
                    "Welcome to the Inner Sanctum...",
                    "You must face the four corner glyph challenges to proceed!"
                };
                ui.startNPCDialogue("Oracle", oracleMap6Dialogue);
            }

                int playerCol =
                    player.x / tileSize;

                int playerRow =
                    player.y / tileSize;

                int[][] glyphCoords = {

                    {2, 2, 2},
                    {13, 2, 3},
                    {2, 9, 4},
                    {13, 9, 5}
                };

                for (
                    int[] gCoord :
                    glyphCoords
                ) {

                    int gCol =
                        gCoord[0];

                    int gRow =
                        gCoord[1];

                    int gNum =
                        gCoord[2];

                    if (
                        Math.abs(
                            playerCol - gCol
                        ) <= 1 &&

                        Math.abs(
                            playerRow - gRow
                        ) <= 1 &&

                        keyH.enterPressed
                    ) {

                        keyH.enterPressed = false;

                        if (
                            !ui.glyphSolved[gNum]
                        ) {

                            ui.startGlyphChallenge(
                                gNum
                            );
                        }

                        break;
                    }
                }
            }
        }

        // =====================================================
        // CUTSCENE
        // =====================================================

        else if (
            gameState == cutsceneState
        ) {

            ui.updateCutscene();
        }

        // =====================================================
        // DIALOGUE
        // =====================================================

        else if (
            gameState == dialogueState
        ) {

            // Handled via KeyHandler
        }
    }

    // =====================================================
    // DRAW GAME
    // =====================================================

    @Override
    public void paintComponent(Graphics g) {

        

        super.paintComponent(g);

        Graphics2D g2 =
            (Graphics2D) g;

        int shakeX = 0;
        int shakeY = 0;

        // =================================================
        // SCREEN SHAKE
        // =================================================

        if (
            screenShakeCounter > 0
        ) {

            int intensity = 6;

            shakeX =
                shakeRandom.nextInt(
                    intensity * 2
                )
                - intensity;

            shakeY =
                shakeRandom.nextInt(
                    intensity * 2
                )
                - intensity;

            g2.translate(
                shakeX,
                shakeY
            );
        }

        // =================================================
        // DRAW MAP
        // =================================================

        tileM.draw(g2);

        // =================================================
        // DARK MAP 8
        // =================================================

        if (
            tileM.currentMap == 8
        ) {

            g2.setColor(
                Color.BLACK
            );

            g2.fillRect(
                0,
                0,
                screenWidth,
                screenHeight
            );
        }

        player.draw(g2);

        // =================================================
        // GUIDE BOOK
        // =================================================

        if (
            tileM.currentMap == 1 &&
            guideBook.visible
        ) {

            guideBook.draw(g2);
        }

        // =================================================
        // ORACLE
        // =================================================

        if (

            // Map 0 Oracle
            (
                tileM.currentMap == 0 &&
                oracleAppeared &&
                !hasBeenToDarkPlace
            )

            ||

            // Map 1 Oracle
            (
                tileM.currentMap == 1
            )

            ||

            // Map 6 Oracle
            (
                tileM.currentMap == 6 &&
                map6OracleAppeared
            )

            ||

            // Map 8 SMALL Oracle
            (
                tileM.currentMap == 8 &&
                !map8OracleInteracted
            )

            ||

            // Map 8 BIG Oracle
            (
                tileM.currentMap == 8 &&
                map8OracleAppeared
            )
        ) {

            // Map 1 Oracle disappears after dropping book
            if (
                tileM.currentMap != 1 ||
                !oracleDisappeared
            ) {

                oracle.draw(g2);
            }
        }

        // =================================================
        // DYING NPC
        // =================================================

        if (
            tileM.currentMap == 7
        ) {

            dyingNpc.draw(g2);
        }

        // =================================================
        // PLAYER
        // =================================================

        player.draw(g2);

        // =================================================
        // MAP 8 WIND
        // =================================================

        if (
            tileM.currentMap == 8
        ) {

            g2.setColor(
                new Color(
                    255,
                    255,
                    255,
                    130
                )
            );

            for (
                int i = 0;
                i < windParticles.length;
                i++
            ) {

                g2.drawLine(

                    windParticles[i].x,
                    windParticles[i].y,

                    windParticles[i].x
                        + windParticles[i].length,

                    windParticles[i].y - 2
                );
            }
        }

        // =================================================
        // REMOVE SCREEN SHAKE
        // =================================================

        if (
            screenShakeCounter > 0
        ) {

            g2.translate(
                -shakeX,
                -shakeY
            );
        }

        // =================================================
        // OBJECTIVES
        // =================================================

        if (
            possessesGuideBook &&
            !villagePathUnlocked &&
            tileM.currentMap == 0 &&
            gameState == playState
        ) {

            g2.setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    16
                )
            );

            g2.setColor(
                Color.YELLOW
            );

            g2.drawString(
                "Press [B] to open Disaster Guide Book",
                20,
                30
            );

        }

        else if (
            villagePathUnlocked &&
            tileM.currentMap == 0 &&
            gameState == playState
        ) {

            g2.setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    16
                )
            );

            g2.setColor(
                Color.GREEN
            );

            g2.drawString(
                "Objective: A pathway to your house appeared",
                20,
                30
            );
        }

        else if (
            tileM.currentMap == 2 &&
            gameState == playState
        ) {

            g2.setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    16
                )
            );

            g2.setColor(
                Color.GREEN
            );

            g2.drawString(
                "Objective: Follow the Long Road North to your house",
                20,
                30
            );
        }

        else if (
            tileM.currentMap == 3 &&
            gameState == playState
        ) {

            g2.setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    16
                )
            );

            g2.setColor(
                Color.CYAN
            );

            g2.drawString(
                "Location: Home",
                20,
                30
            );
        }

        else if (
            tileM.currentMap == 6 &&
            gameState == playState
        ) {

            g2.setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    16
                )
            );

            g2.setColor(
                Color.CYAN
            );

            g2.drawString(
                "Objective: Stand near corner glyphs (2, 3, 4, 5) and press [ENTER]",
                20,
                30
            );
        }

        else if (
            tileM.currentMap == 4 &&
            gameState == playState
        ) {

            g2.setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    16
                )
            );

            g2.setColor(
                Color.ORANGE
            );

            g2.drawString(
                "Objective: Inspect the Glyph and press [ENTER]",
                20,
                30
            );

            g2.setColor(
                Color.ORANGE
            );

            g2.fillRect(
                tileSize * 4,
                tileSize * 6,
                tileSize,
                tileSize
            );

            g2.setColor(
                Color.WHITE
            );

            g2.drawString(
                "GLYPH",
                tileSize * 4,
                tileSize * 6 + 28
            );
        }

        else if (
            tileM.currentMap == 0 &&
            !hasBeenToDarkPlace &&
            gameState == playState
        ) {

            g2.setFont(
                new Font(
                    "Arial",
                    Font.BOLD,
                    16
                )
            );

            g2.setColor(
                Color.WHITE
            );

            g2.drawString(
                "Objective: Chop Trees ("
                + treesChopped
                + "/"
                + MAX_TREES_TO_CHOP
                + ")",
                20,
                30
            );

            g2.drawString(
                "Press [E] to chop",
                20,
                50
            );
        }

        // =================================================
        // UI STATES
        // =================================================

        if (gameState == puzzleState 
            && tileM.currentMap == 8
            ) {
            
            ui.drawOracleQuestion(g2);
        }

        if (
            gameState == cutsceneState
        ) {

            ui.drawCutscene(g2);

        }

        else if (
            gameState == dialogueState
        ) {

            ui.drawNPCDialogue(g2);

        }

        else if (
            gameState == puzzleState
        ) {

            if (
                tileM.currentMap == 4
            ) {

                ui.drawEarthquakePuzzle(g2);

            }

            else if (
                tileM.currentMap == 6
            ) {

                ui.drawGlyphPuzzle(g2);
            }
        }
            else if (
                gameState == creditsState
            ) {
                
                ui.drawCredits(g2);
            
            }

        // =================================================
        // GUIDE BOOK UI
        // =================================================

        if (
            possessesGuideBook &&
            keyH.bookPressed
        ) {

            ui.drawGuideBookUI(g2);
        }

        g2.dispose();
    }
}