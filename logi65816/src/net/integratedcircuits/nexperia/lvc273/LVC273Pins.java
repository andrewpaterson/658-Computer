package net.integratedcircuits.nexperia.lvc273;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC273Pins
    extends Pins<LVC273Snapshot, LVC273Pins, LVC273>
{
  PinValue getCP();

  BusValue getInput();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  PinValue getMRB();

  void setOutputHighImpedance();
}

