package com.cultivatingcows.Models;

/**
 * Created by colleenminor on 11/15/15.
 */
public class PairOfDice {
        public int die1;   // Number showing on the first die.
        public int die2;   // Number showing on the second die.
        public PairOfDice() {
            // Constructor.  Rolls the dice, so that they initially
            // show some random values.
            roll();  // Call the roll() method to roll the dice.
        }
        public void roll() {
            // Roll the dice by setting each of the dice to be
            // a random number between 1 and 6.
            die1 = (int)(Math.random()*6) + 1;
            die2 = (int)(Math.random()*6) + 1;
        }
}
