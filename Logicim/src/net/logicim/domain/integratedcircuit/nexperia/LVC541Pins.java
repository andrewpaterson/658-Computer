package net.logicim.domain.integratedcircuit.nexperia;

import net.logicim.domain.common.*;

public class LVC541Pins
    extends Pins
{
  protected Omniport y;
  protected Omniport a;
  protected Uniport oe1B;
  protected Uniport oe2B;

  public LVC541Pins(Timeline timeline)
  {
    super(timeline);
  }

  void setYError()
  {
  }

  void setYUnsettled()
  {
  }

  void setYHighImpedance()
  {
  }

  void setYValue(long value)
  {
    y.writeAllPinsBool(value);
  }
}

