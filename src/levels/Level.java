package levels;

public class Level {
    private int[][] LevelData;
    public Level(int[][] LevelData) {
        this.LevelData = LevelData;
    }

    public int getSpriteIndex(int x, int y) {
        return LevelData[y][x];
    }

    public int[][] getLevelData() {
        return LevelData;
    }
}
