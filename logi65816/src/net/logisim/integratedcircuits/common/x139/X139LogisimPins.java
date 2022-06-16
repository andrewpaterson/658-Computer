package net.logisim.integratedcircuits.common.x139;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.common.x139.X139;
import net.integratedcircuits.common.x139.X139Pins;
import net.integratedcircuits.common.x139.X139Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc139.LVC139Factory.*;

public class X139LogisimPins
    extends LogisimPins<X139Snapshot, X139Pins, X139>
    implements X139Pins
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

