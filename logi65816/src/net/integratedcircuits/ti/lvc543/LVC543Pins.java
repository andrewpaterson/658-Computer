package net.integratedcircuits.ti.lvc543;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC543Pins
    extends Pins<LVC543Snapshot, LVC543Pins, LVC543>
{
  PinValue getLEB(int index);

  PinValue getCEB(int index);

  PinValue getOEB(int index);

  BusValue getInput(int index);

  void setOutputUnsettled(int index);

  void setOutputError(int index);

  void setOutput(int index, long latchValue);

  void setOutputHighImpedance(int index);
}

