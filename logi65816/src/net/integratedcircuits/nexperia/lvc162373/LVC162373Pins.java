package net.integratedcircuits.nexperia.lvc162373;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC162373Pins
    extends Pins<LVC162373Snapshot, LVC162373Pins, LVC162373>
{
  PinValue getLE(int index);

  BusValue getInput(int index);

  void setOutputUnsettled(int index);

  void setOutputError(int index);

  void setOutput(int index, long latchValue);

  PinValue getOEB(int index);

  void setOutputHighImpedance(int index);
}

