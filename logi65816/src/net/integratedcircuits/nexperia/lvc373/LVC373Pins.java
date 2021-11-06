package net.integratedcircuits.nexperia.lvc373;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC373Pins
    extends Pins<LVC373Snapshot, LVC373Pins, LVC373>
{
  PinValue getLE();

  BusValue getInput();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  PinValue getOEB();

  void setOutputHighImpedance();
}

