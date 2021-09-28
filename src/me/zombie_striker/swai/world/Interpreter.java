package me.zombie_striker.swai.world;

import me.zombie_striker.swai.data.PersonalityMatrix;

public interface Interpreter {

   void loseScore(PersonalityMatrix matrix, int score);
   void increaseScore(PersonalityMatrix matrix, int score);
   void onTerminate(PersonalityMatrix matrix);

   int getScore(PersonalityMatrix matrix);
}
