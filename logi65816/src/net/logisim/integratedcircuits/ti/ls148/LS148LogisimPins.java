package net.logisim.integratedcircuits.ti.ls148;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.ti.ls148.LS148;
import net.integratedcircuits.ti.ls148.LS148Pins;
import net.integratedcircuits.ti.ls148.LS148Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.ti.ls148.LS148Factory.*;

public class LS148LogisimPins
    extends LogisimPins<LS148Snapshot, LS148Pins, LS148>
    implements LS148Pins
{
  @Override
  public PinValue getEI()
  {
    return getValue(PORT_EI);
  }

  @Override
  public BusValue getInput()
  {
    return getValue(PORT_INPUT);
  }

  @Override
  public void setOutputError()
  {
    setError(PORT_A);
    setError(PORT_EO);
    setError(PORT_GS);
  }

  @Override
  public void setOutputUnsettled()
  {
  }

  @Override
  public void setA(long value)
  {
    setValue(PORT_A, value);
  }

  @Override
  public void setGS(boolean value)
  {
    setValue(PORT_GS, value);
  }

  @Override
  public void setEO(boolean value)
  {
    setValue(PORT_EO, value);
  }
}

