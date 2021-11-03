package net.integratedcircuits.nexperia.lvc161;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC161Pins
    extends Pins<LVC161Snapshot, LVC161Pins, LVC161>
{
  BusValue getInput();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  boolean isParallelLoad();

  PinValue getMRB();

  boolean isClock();

  PinValue getCEP();

  PinValue getCET();

  void setCarryUnsettled();

  void setCarryError();

  void setCarry(boolean value);
}

