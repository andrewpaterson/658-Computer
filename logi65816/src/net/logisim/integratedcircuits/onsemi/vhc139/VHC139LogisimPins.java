package net.logisim.integratedcircuits.onsemi.vhc139;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.onsemi.vhc139.VHC139;
import net.integratedcircuits.onsemi.vhc139.VHC139Pins;
import net.integratedcircuits.onsemi.vhc139.VHC139Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.onsemi.vhc139.VHC139Factory.*;

public class VHC139LogisimPins
    extends LogisimPins<VHC139Snapshot, VHC139Pins, VHC139>
    implements VHC139Pins
{
  @Override
  public PinValue getEB(int port)
  {
    return getValue(PORT_EB[port]);
  }

  @Override
  public BusValue getA(int port)
  {
    return getValue(PORT_A[port]);
  }

  @Override
  public void setOutputError(int port)
  {
    setError(PORT_Y[port]);
  }

  @Override
  public void setOutputUnsettled(int port)
  {
  }

  @Override
  public void setOutput(int port, long outputValue)
  {
    setValue(PORT_Y[port], outputValue);
  }
}

