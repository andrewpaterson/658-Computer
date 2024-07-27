package net.logicim.ui.simulation.component.passive.pin;

public class PinViewNumber
{
  public PinView pinView;
  public String name;
  public Integer number;

  public PinViewNumber(PinView pinView, String name, Integer number)
  {
    this.pinView = pinView;
    this.name = name;
    this.number = number;
  }
}

