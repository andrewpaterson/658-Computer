package net.logisim.integratedcircuits.nexperia.lvc126;

import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc126.LVC126;
import net.integratedcircuits.nexperia.lvc126.LVC126Pins;
import net.integratedcircuits.nexperia.lvc126.LVC126Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc126.LVC126Factory.*;

public class LVC126LogisimPins
    extends LogisimPins<LVC126Snapshot, LVC126Pins, LVC126>
    implements LVC126Pins
{
  public LVC126LogisimPins()
  {
  }

  @Override
  public void setYError(int port)
  {
    setError(PORT_Y[port]);
  }

  @Override
  public void setYUnsettled(int port)
  {
  }

  @Override
  public void setYHighImpedance(int port)
  {
    setHighImpedance(PORT_Y[port]);
  }

  @Override
  public void setYValue(int port, boolean value)
  {
    setValue(PORT_Y[port], value);
  }

  @Override
  public PinValue getOE(int port)
  {
    return getValue(PORT_OE[port]);
  }

  @Override
  public PinValue getAValue(int port)
  {
    return getValue(PORT_A[port]);
  }
}

