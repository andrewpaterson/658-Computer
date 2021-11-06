package net.integratedcircuits.ti.f283;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface F283Pins
    extends Pins<F283Snapshot, F283Pins, F283>
{
  BusValue getA();

  BusValue getB();

  PinValue getC0();

  void setCOError();

  void setCOUnsettled();

  void setCO(boolean value);

  void setSigmaError();

  void setSigmaUnsettled();

  void setSigma(long value);
}

