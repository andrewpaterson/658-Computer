package net.integratedcircuits.common.x139;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface X139Pins
    extends Pins<X139Snapshot, X139Pins, X139>
{
  PinValue getEB(int port);

  BusValue getA(int port);

  void setOutputError(int port);

  void setOutputUnsettled(int port);

  void setOutput(int port, long outputValue);
}

