package net.integratedcircuits.nexperia.lvc16373;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC16373Pins
    extends Pins<LVC16373Snapshot, LVC16373Pins, LVC16373>
{
  PinValue getLE(int index);

  BusValue getInput(int index);

  void setOutputUnsettled(int index);

  void setOutputError(int index);

  void setOutput(int index, long latchValue);

  PinValue getOEB(int index);

  void setOutputHighImpedance(int index);
}

