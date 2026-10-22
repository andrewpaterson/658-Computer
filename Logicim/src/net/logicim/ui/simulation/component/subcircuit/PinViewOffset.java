package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.ui.simulation.component.passive.pin.PinView;

public class PinViewOffset
{
  public PinView pinView;
  public int pinOffset;

  public PinViewOffset(PinView pinView, int pinOffset)
  {
    this.pinView = pinView;
    this.pinOffset = pinOffset;
  }
}

