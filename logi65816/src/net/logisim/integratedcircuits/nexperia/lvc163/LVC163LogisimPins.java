package net.logisim.integratedcircuits.nexperia.lvc163;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc163.LVC163;
import net.integratedcircuits.nexperia.lvc163.LVC163Pins;
import net.integratedcircuits.nexperia.lvc163.LVC163Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc163.LVC163Factory.*;

public class LVC163LogisimPins
    extends LogisimPins<LVC163Snapshot, LVC163Pins, LVC163>
    implements LVC163Pins
{
  @Override
  public BusValue getInput()
  {
    return getValue(LVC163Factory.PORT_D);
  }

  @Override
  public void setOutputUnsettled()
  {
  }

  @Override
  public void setOutputError()
  {
  }

  @Override
  public void setOutput(long latchValue)
  {
    setValue(PORT_Q, latchValue);
  }

  @Override
  public PinValue getPEB()
  {
    return getValue(PORT_PEB);
  }

  @Override
  public PinValue getMRB()
  {
    return getValue(PORT_MRB);
  }

  @Override
  public PinValue isClock()
  {
    return getValue(PORT_CP);
  }

  @Override
  public PinValue getCEP()
  {
    return getValue(PORT_CEP);
  }

  @Override
  public PinValue getCET()
  {
    return getValue(PORT_CET);
  }

  @Override
  public void setCarryUnsettled()
  {
  }

  @Override
  public void setCarryError()
  {
  }

  @Override
  public void setCarry(boolean value)
  {
    setValue(PORT_TC, value);
  }
}

