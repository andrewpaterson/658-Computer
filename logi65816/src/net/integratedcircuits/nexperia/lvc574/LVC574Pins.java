package net.integratedcircuits.nexperia.lvc574;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC574Pins
    extends Pins<LVC574Snapshot, LVC574Pins, LVC574>
{
  PinValue getClock();

  BusValue getInput();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  PinValue getOEB();

  void setOutputHighImpedance();
}

