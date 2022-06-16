package net.integratedcircuits.common.x4040;

import net.common.PinValue;
import net.common.Pins;

public interface X4040Pins
    extends Pins<X4040Snapshot, X4040Pins, X4040>
{
  PinValue getMasterResetB();

  PinValue getClock();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);
}

