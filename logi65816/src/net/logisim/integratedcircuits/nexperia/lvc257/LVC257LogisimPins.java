package net.logisim.integratedcircuits.nexperia.lvc257;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc257.LVC257;
import net.integratedcircuits.nexperia.lvc257.LVC257Pins;
import net.integratedcircuits.nexperia.lvc257.LVC257Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc257.LVC257Factory.*;

public class LVC257LogisimPins
    extends LogisimPins<LVC257Snapshot, LVC257Pins, LVC257>
    implements LVC257Pins
{
  public LVC257LogisimPins()
  {
  }

  @Override
  public void setYError()
  {
    setError(PORT_Y);
  }

  @Override
  public void setYUnsettled()
  {
  }

  @Override
  public void setYHighImpedance()
  {
    setHighImpedance(PORT_Y);
  }

  @Override
  public PinValue getSelector()
  {
    return getValue(PORT_S);
  }

  @Override
  public PinValue getOEB()
  {
    return getValue(PORT_OEB);
  }

  @Override
  public BusValue getInputValue(int port)
  {
    return getValue(PORT_INPUT[port]);
  }

  @Override
  public void setYValue(long value)
  {
    setValue(PORT_Y, value);
  }
}

