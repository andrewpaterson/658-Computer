package net.integratedcircuits.nexperia.lvc138;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC138Pins
    extends Pins<LVC138Snapshot, LVC138Pins, LVC138>
{
  PinValue getE1B();

  PinValue getE2B();

  PinValue getE3();

  BusValue getA();

  void setOutputError();

  void setOutputUnsettled();

  void setOutput(long outputValue);
}

