package net.logisim.integratedcircuits.nexperia.lvc161;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc161.LVC161;
import net.integratedcircuits.nexperia.lvc161.LVC161Pins;
import net.integratedcircuits.nexperia.lvc161.LVC161Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc161.LVC161Factory.*;

public class LVC161LogisimPins
    extends LogisimPins<LVC161Snapshot, LVC161Pins, LVC161>
    implements LVC161Pins
{
  @Override
  public BusValue getInput()
  {
    return getValue(LVC161Factory.PORT_D);
  }

  @Override
  public void setOutputUnsettled()
  {
  }

  @Override
  public void setOutputError()
  {
    setError(PORT_Q);
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
  public PinValue getClock()
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
    setError(PORT_TC);
  }

  @Override
  public void setCarry(boolean value)
  {
    setValue(PORT_TC, value);
  }
}

