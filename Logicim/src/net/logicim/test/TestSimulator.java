package net.logicim.test;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.integratedcircuit.nexperia.LVC541;

public class TestSimulator
{
  public static void main(String[] args)
  {
    Timeline timeline = new Timeline();
    new LVC541("Henry", timeline);

  }
}

