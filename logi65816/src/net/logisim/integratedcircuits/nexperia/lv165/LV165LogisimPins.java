package net.logisim.integratedcircuits.nexperia.lv165;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lv165.LV165;
import net.integratedcircuits.nexperia.lv165.LV165Pins;
import net.integratedcircuits.nexperia.lv165.LV165Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lv165.LV165Factory.*;

public class LV165LogisimPins
    extends LogisimPins<LV165Snapshot, LV165Pins, LV165>
    implements LV165Pins
{
  public LV165LogisimPins()
  {
  }

  @Override
  public PinValue getPLB()
  {
    return getValue(PORT_PLB);
  }

  @Override
  public PinValue getCP()
  {
    return getValue(PORT_CP);
  }

  @Override
  public BusValue getD()
  {
    return getValue(PORT_D);
  }

  @Override
  public PinValue getCEB()
  {
    return getValue(PORT_CEB);
  }

  @Override
  public PinValue getDS()
  {
    return getValue(PORT_DS);
  }

  @Override
  public void setQValue(boolean value)
  {
    setValue(PORT_Q, value);
    setValue(PORT_QB, !value);
  }
}

