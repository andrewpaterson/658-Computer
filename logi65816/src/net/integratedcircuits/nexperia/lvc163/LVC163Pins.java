package net.integratedcircuits.nexperia.lvc163;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC163Pins
    extends Pins<LVC163Snapshot, LVC163Pins, LVC163>
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

