package net.integratedcircuits.nexperia.lvc139;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC139Pins
    extends Pins<LVC139Snapshot, LVC139Pins, LVC139>
{
  PinValue getEB(int port);

  BusValue getA(int port);

  void setOutputError(int port);

  void setOutputUnsettled(int port);

  void setOutput(int port, long outputValue);
}

