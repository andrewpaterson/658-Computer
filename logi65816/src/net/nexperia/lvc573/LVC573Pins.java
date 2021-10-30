package net.nexperia.lvc573;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC573Pins
    extends Pins<LVC573Snapshot, LVC573Pins, LVC573>
{
  PinValue getLE();

  BusValue getInput();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  PinValue getOEB();

  void setOutputHighImpedance();
}

