package utilz;

import main.Game;

public class Constants {
    public static final float GRAVITY = 0.04f * Game.SCALE;

    public static class EnemyConstants{
        public static final int SKELETON = 0;

        public static final int IDLE = 3;
        public static final int ATTACK = 0;
        public static final int RUN = 2;
        public static final int HIT = 4;
        public static final int DEAD = 1;

        public static final int SKELETON_WIDTH_DEFAULT = 64;
        public static final int SKELETON_HEIGHT_DEFAULT = 64;

        public static final int SKELETON_WIDTH = (int)(SKELETON_WIDTH_DEFAULT * Game.SCALE * 0.8);
        public static final int SKELETON_HEIGHT = (int)(SKELETON_HEIGHT_DEFAULT * Game.SCALE * 0.8);

        public static final int SKELETON_DRAW_X = (int)(17 * Game.SCALE);
        public static final int SKELETON_DRAW_Y = (int)(13 * Game.SCALE);

        public static int GetMaxHealth(int enemy_type) {
            switch (enemy_type) {
                case SKELETON:
                    return 10;
                default:
                    return 1;
            }
        }

        public static int GetEnemyDmg(int enemy_type) {
            switch (enemy_type) {
                case SKELETON:
                    return 5;
                default:
                    return 0;
            }

        }

        public static int GetSpriteAmounts(int enemyType, int enemyState){
            switch (enemyType){
                case SKELETON:
                    switch (enemyState){
                        case IDLE:
                            return 4;
                        case ATTACK:
                        case DEAD:
                            return 13;
                        case RUN:
                            return 12;
                        case HIT:
                            return 3;
                    }
            }
            return 0;
        }
    }

    public static class Environment{
        public static final int BIG_CLOUDS_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUDS_HEIGHT_DEFAULT = 101;
        public static final int SMALL_CLOUDS_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUDS_HEIGHT_DEFAULT = 24;

        public static final int BIG_CLOUDS_WIDTH = (int)(BIG_CLOUDS_WIDTH_DEFAULT * Game.SCALE);
        public static final int BIG_CLOUDS_HEIGHT = (int)(BIG_CLOUDS_HEIGHT_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUDS_WIDTH = (int)(SMALL_CLOUDS_WIDTH_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUDS_HEIGHT = (int)(SMALL_CLOUDS_HEIGHT_DEFAULT * Game.SCALE);
    }

    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE);
            public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons{
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE);
        }

        public static class UrmButtons{
            public static final int URM_SIZE_DEFAULT = 56;
            public static final int URM_SIZE = (int)(URM_SIZE_DEFAULT * Game.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
            public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

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
