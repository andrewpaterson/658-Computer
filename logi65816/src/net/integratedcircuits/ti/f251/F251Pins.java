package net.integratedcircuits.ti.f251;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface F251Pins
    extends Pins<F251Snapshot, F251Pins, F251>
{
  PinValue getGB();

  BusValue getA();

  BusValue getD();

  void setOutputError();

  void setOutputUnsettled();

  void setOutputHighImpedance();

  void setOutput(boolean yValue, boolean wValue);
}

