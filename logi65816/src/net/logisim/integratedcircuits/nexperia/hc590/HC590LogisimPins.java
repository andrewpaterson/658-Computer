package net.logisim.integratedcircuits.nexperia.hc590;

import net.common.PinValue;
import net.integratedcircuits.nexperia.hc590.HC590;
import net.integratedcircuits.nexperia.hc590.HC590Pins;
import net.integratedcircuits.nexperia.hc590.HC590Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.hc590.HC590Factory.*;

public class HC590LogisimPins
    extends LogisimPins<HC590Snapshot, HC590Pins, HC590>
    implements HC590Pins
{
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
  public PinValue getMasterResetB()
  {
    return getValue(PORT_MRCB);
  }

  @Override
  public PinValue getClock()
  {
    return getValue(PORT_CPC);
  }

  @Override
  public PinValue getCountEnabledB()
  {
    return getValue(PORT_CEB);
  }

  @Override
  public PinValue getCounterToRegisterClock()
  {
    return getValue(PORT_CPR);
  }

  @Override
  public PinValue getOutputEnabledB()
  {
    return getValue(PORT_OEB);
  }

  @Override
  public void setOutputHighImpedance()
  {
    setHighImpedance(PORT_Q);
  }

  @Override
  public void setCarry(boolean value)
  {
    setValue(PORT_RCOB, value);
  }
}

