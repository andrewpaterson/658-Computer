package net.integratedcircuits.ti.f521;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface F521Pins
    extends Pins<F521Snapshot, F521Pins, F521>
{
  PinValue getLE();

  BusValue getInput();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  PinValue getOEB();

  void setOutputHighImpedance();
}

