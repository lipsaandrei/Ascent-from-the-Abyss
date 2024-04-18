package utilz;

public class Constants {
    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 6;
        public static final int RUNNING = 8;
        public static final int JUMP = 7;
        public static final int DEATH = 4;
        public static final int FALL = 5;
        public static final int HIT = 9;
        public static final int ATTACK_1 = 0;
        public static final int ATTACK_2 = 1;
        public static final int ATTACK_3 = 2;
        public static final int ATTACK_4 = 3;
        public static final int HIT_White = 10;

        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case RUNNING:
                case IDLE:
                    return 8;
                case JUMP:
                case FALL:
                    return 2;
                case ATTACK_1:
                case ATTACK_2:
                case ATTACK_3:
                case ATTACK_4:
                case HIT:
                case HIT_White:
                    return 4;
                case DEATH:
                    return 6;
                default:
                    return 1;
            }
        }
    }
}
