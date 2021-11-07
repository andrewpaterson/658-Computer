package net.integratedcircuits.nexperia.hc4040;

import net.common.PinValue;
import net.common.Pins;

public interface HC4040Pins
    extends Pins<HC4040Snapshot, HC4040Pins, HC4040>
{
  PinValue getMasterResetB();

  PinValue getClock();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);
}

