package net.integratedcircuits.onsemi.vhc139;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface VHC139Pins
    extends Pins<VHC139Snapshot, VHC139Pins, VHC139>
{
  PinValue getEB(int port);

  BusValue getA(int port);

  void setOutputError(int port);

  void setOutputUnsettled(int port);

  void setOutput(int port, long outputValue);
}

