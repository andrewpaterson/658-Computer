package net.logisim.integratedcircuits.nexperia.lvc595;

import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc595.LVC595;
import net.integratedcircuits.nexperia.lvc595.LVC595Pins;
import net.integratedcircuits.nexperia.lvc595.LVC595Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc595.LVC595Factory.*;

public class LVC595LogisimPins
    extends LogisimPins<LVC595Snapshot, LVC595Pins, LVC595>
    implements LVC595Pins
{
  public LVC595LogisimPins()
  {
  }

  @Override
  public void setQ(int value)
  {
    setValue(PORT_Q, value);
  }

  @Override
  public void setQ7S(boolean value)
  {
    setValue(PORT_Q7S, value);
  }

  @Override
  public PinValue getMRB()
  {
    return getValue(PORT_MRB);
  }

  @Override
  public PinValue getSHCP()
  {
    return getValue(PORT_SHCP);
  }

  @Override
  public PinValue getSTCP()
  {
    return getValue(PORT_STCP);
  }

  @Override
  public PinValue getOEB()
  {
    return getValue(PORT_OEB);
  }

  @Override
  public PinValue getDS()
  {
    return getValue(PORT_DS);
  }

  @Override
  public void setQError()
  {
    setError(PORT_Q);
  }

  @Override
  public void setQ7SError()
  {
    setError(PORT_Q7S);
  }

  @Override
  public void setQImpedance()
  {
    setHighImpedance(PORT_Q);
  }
}

