package net.integratedcircuits.nexperia.hc590;

import net.common.PinValue;
import net.common.Pins;

public interface HC590Pins
    extends Pins<HC590Snapshot, HC590Pins, HC590>
{
  PinValue getMasterResetB();

  PinValue getCountEnabledB();

  PinValue getCounterToRegisterClock();

  PinValue getOutputEnabledB();

  void setOutputHighImpedance();

  PinValue getClock();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  void setCarry(boolean value);
}

