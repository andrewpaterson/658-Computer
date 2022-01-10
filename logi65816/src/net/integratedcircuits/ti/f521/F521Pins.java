package net.integratedcircuits.ti.f521;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface F521Pins
    extends Pins<F521Snapshot, F521Pins, F521>
{
  BusValue getP();

  BusValue getQ();

  PinValue getOEB();

  void setQEqualPError();

  void setQEqualPUnsettled();

  void setQEqualP(boolean value);
}

