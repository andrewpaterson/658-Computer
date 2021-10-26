package net.nexperia.lvc573;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC573Pins
    extends Pins
{
  void setLatch(LVC573 latch);

  PinValue getLE();

  BusValue getInput();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  PinValue getOEB();
}

