package net.logisim.integratedcircuits.nexperia.lvc139;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc139.LVC139;
import net.integratedcircuits.nexperia.lvc139.LVC139Pins;
import net.integratedcircuits.nexperia.lvc139.LVC139Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc139.LVC139Factory.*;

public class LVC139LogisimPins
    extends LogisimPins<LVC139Snapshot, LVC139Pins, LVC139>
    implements LVC139Pins
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

