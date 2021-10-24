package net.nexperia.lvc4245;

public class LVC4245
{
  private LVC4245Pins pins;

  public LVC4245(LVC4245Pins pins)
  {
    this.pins = pins;
    this.pins.setTransceiver(this);
  }
}

