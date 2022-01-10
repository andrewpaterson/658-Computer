package net.logisim.integratedcircuits.ti.f521;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.ti.f521.F521;
import net.integratedcircuits.ti.f521.F521Pins;
import net.integratedcircuits.ti.f521.F521Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.ti.f521.F521Factory.*;

public class F521LogisimPins
    extends LogisimPins<F521Snapshot, F521Pins, F521>
    implements F521Pins
{
  @Override
  public BusValue getP()
  {
    return getValue(PORT_P);
  }

  @Override
  public BusValue getQ()
  {
    return getValue(PORT_Q);
  }

  @Override
  public PinValue getOEB()
  {
    return getValue(PORT_OEB);
  }

  @Override
  public void setQEqualPError()
  {
    setError(PORT_P_EQUALS_Q);
  }

  @Override
  public void setQEqualPUnsettled()
  {
  }

  @Override
  public void setQEqualP(boolean value)
  {
    setValue(PORT_P_EQUALS_Q, value);
  }
}

