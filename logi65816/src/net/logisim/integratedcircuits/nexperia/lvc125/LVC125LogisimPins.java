package net.logisim.integratedcircuits.nexperia.lvc125;

import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc125.LVC125;
import net.integratedcircuits.nexperia.lvc125.LVC125Pins;
import net.integratedcircuits.nexperia.lvc125.LVC125Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc125.LVC125Factory.*;

public class LVC125LogisimPins
    extends LogisimPins<LVC125Snapshot, LVC125Pins, LVC125>
    implements LVC125Pins
{
  public LVC125LogisimPins()
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

