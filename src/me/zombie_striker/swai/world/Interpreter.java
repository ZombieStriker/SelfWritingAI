package me.zombie_striker.swai.world;

import me.zombie_striker.swai.data.PersonalityMatrix;

import java.util.List;

public interface Interpreter {

   void loseScore(PersonalityMatrix matrix, int score);
   void increaseScore(PersonalityMatrix matrix, int score);
   void onTerminate(PersonalityMatrix matrix);
   List<PersonalityMatrix> getMatrices();

   int getScore(PersonalityMatrix matrix);

   boolean getWarpSpeed();
   void setWarpSpeed(boolean warpSpeed);
   boolean hasEnabledFineTuning();
   void setFineTuning(boolean b) ;

    void divideScoreBy(PersonalityMatrix matrix, int i);
}
