package net.logisim.integratedcircuits.diodesinc.b3251;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.diodesinc.pib3b3251.B3251;
import net.integratedcircuits.diodesinc.pib3b3251.B3251Pins;
import net.integratedcircuits.diodesinc.pib3b3251.B3251Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.diodesinc.b3251.B3251Factory.*;

public class B3251LogisimPins
    extends LogisimPins<B3251Snapshot, B3251Pins, B3251>
    implements B3251Pins
{
  @Override
  public BusValue getInput()
  {
    return getValue(PORT_INPUT);
  }

  @Override
  public void setY(boolean value)
  {
    setValue(PORT_Y, value);
  }

  @Override
  public BusValue getSelect()
  {
    return getValue(PORT_SELECT);
  }

  @Override
  public PinValue getEB()
  {
    return getValue(PORT_ENABLE_B);
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
  public void setYError()
  {
    setError(PORT_Y);
  }
}

