package net.logicim.ui.simulation.component.passive.pin;

import net.logicim.ui.common.integratedcircuit.StaticView;

public class StaticViewNumber
{
  public StaticView<?> staticView;
  public String name;
  public Integer number;

  public StaticViewNumber(StaticView<?> staticView, String name, Integer number)
  {
    this.staticView = staticView;
    this.name = name;
    this.number = number;
  }
}

