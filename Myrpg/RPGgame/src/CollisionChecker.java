public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Player player) {

        int playerLeftWorldX = player.x + player.solidArea.x;
        int playerRightWorldX = player.x + player.solidArea.x + player.solidArea.width;
        int playerTopWorldY = player.y + player.solidArea.y;
        int playerBottomWorldY = player.y + player.solidArea.y + player.solidArea.height;

        int playerLeftCol = playerLeftWorldX / gp.tileSize;
        int playerRightCol = playerRightWorldX / gp.tileSize;
        int playerTopRow = playerTopWorldY / gp.tileSize;
        int playerBottomRow = playerBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        // Get the dynamic bounds of the current map (handles 50-column maps like Map 5 safely)
        int currentMapMaxCol = gp.tileM.mapTileNum[gp.tileM.currentMap].length;
        int currentMapMaxRow = gp.tileM.mapTileNum[gp.tileM.currentMap][0].length;

        switch (player.direction) {
            case "up":
                playerTopRow = (playerTopWorldY - player.speed) / gp.tileSize;
                if (playerTopRow >= 0 && playerTopRow < currentMapMaxRow && playerLeftCol >= 0 && playerRightCol < currentMapMaxCol) {
                    tileNum1 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerLeftCol][playerTopRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerRightCol][playerTopRow];
                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        player.collisionOn = true;
                    }
                }
                break;

            case "down":
                playerBottomRow = (playerBottomWorldY + player.speed) / gp.tileSize;
                if (playerBottomRow >= 0 && playerBottomRow < currentMapMaxRow && playerLeftCol >= 0 && playerRightCol < currentMapMaxCol) {
                    tileNum1 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerLeftCol][playerBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerRightCol][playerBottomRow];
                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        player.collisionOn = true;
                    }
                }
                break;

            case "left":
                playerLeftCol = (playerLeftWorldX - player.speed) / gp.tileSize;
                if (playerLeftCol >= 0 && playerLeftCol < currentMapMaxCol && playerTopRow >= 0 && playerBottomRow < currentMapMaxRow) {
                    tileNum1 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerLeftCol][playerTopRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerLeftCol][playerBottomRow];
                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        player.collisionOn = true;
                    }
                }
                break;

            case "right":
                playerRightCol = (playerRightWorldX + player.speed) / gp.tileSize;
                if (playerRightCol >= 0 && playerRightCol < currentMapMaxCol && playerTopRow >= 0 && playerBottomRow < currentMapMaxRow) {
                    tileNum1 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerRightCol][playerTopRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerRightCol][playerBottomRow];
                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        player.collisionOn = true;
                    }
                }
                break;
        }
    }
}