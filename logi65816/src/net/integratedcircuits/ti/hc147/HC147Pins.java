package net.integratedcircuits.ti.hc147;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface HC147Pins
    extends Pins<HC147Snapshot, HC147Pins, HC147>
{
  BusValue getInput();

  void setOutputError();

  void setOutputUnsettled();

  void setOutput(long value);
}

