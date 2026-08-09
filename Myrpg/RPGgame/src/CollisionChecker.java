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

        switch (player.direction) {
            case "up":
                playerTopRow = (playerTopWorldY - player.speed) / gp.tileSize;
                if (playerTopRow >= 0 && playerLeftCol >= 0 && playerRightCol < gp.maxScreenCol) {
                    tileNum1 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerLeftCol][playerTopRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerRightCol][playerTopRow];
                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        player.collisionOn = true;
                    }
                }
                break;

            case "down":
                playerBottomRow = (playerBottomWorldY + player.speed) / gp.tileSize;
                if (playerBottomRow < gp.maxScreenRow && playerLeftCol >= 0 && playerRightCol < gp.maxScreenCol) {
                    tileNum1 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerLeftCol][playerBottomRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerRightCol][playerBottomRow];
                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        player.collisionOn = true;
                    }
                }
                break;

            case "left":
                playerLeftCol = (playerLeftWorldX - player.speed) / gp.tileSize;
                if (playerLeftCol >= 0 && playerTopRow >= 0 && playerBottomRow < gp.maxScreenRow) {
                    tileNum1 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerLeftCol][playerTopRow];
                    tileNum2 = gp.tileM.mapTileNum[gp.tileM.currentMap][playerLeftCol][playerBottomRow];
                    if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                        player.collisionOn = true;
                    }
                }
                break;

            case "right":
                playerRightCol = (playerRightWorldX + player.speed) / gp.tileSize;
                if (playerRightCol < gp.maxScreenCol && playerTopRow >= 0 && playerBottomRow < gp.maxScreenRow) {
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