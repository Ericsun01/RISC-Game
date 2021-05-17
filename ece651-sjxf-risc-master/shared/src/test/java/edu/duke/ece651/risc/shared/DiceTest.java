package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

public class DiceTest {
  @Test
  public void test_rollDiceWithBonus() {
    Random r = new Random(1);
    Dice u = new Dice(r);
    int res0 = u.rollDiceWithBonus(0);
    assertEquals(res0, 6);
    int res1 = u.rollDiceWithBonus(1);
    assertEquals(res1, 10);
    int res2 = u.rollDiceWithBonus(2);
    assertEquals(res2, 11);
    int res3 = u.rollDiceWithBonus(3);
    assertEquals(res3, 19);
    int res4 = u.rollDiceWithBonus(4);
    assertEquals(res4, 23);
    int res5 = u.rollDiceWithBonus(5);
    assertEquals(res5, 16);
    int res6 = u.rollDiceWithBonus(6);
    assertEquals(res6, 30);
    Dice u1 = new Dice();
  }

}

