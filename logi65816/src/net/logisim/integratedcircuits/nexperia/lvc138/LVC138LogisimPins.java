package net.logisim.integratedcircuits.nexperia.lvc138;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc138.LVC138;
import net.integratedcircuits.nexperia.lvc138.LVC138Pins;
import net.integratedcircuits.nexperia.lvc138.LVC138Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc138.LVC138Factory.*;

public class LVC138LogisimPins
    extends LogisimPins<LVC138Snapshot, LVC138Pins, LVC138>
    implements LVC138Pins
{
  @Override
  public PinValue getE1B()
  {
    return getValue(PORT_E1B);
  }

  @Override
  public PinValue getE2B()
  {
    return getValue(PORT_E2B);
  }

  @Override
  public PinValue getE3()
  {
    return getValue(PORT_E3);
  }

  @Override
  public BusValue getA()
  {
    return getValue(PORT_A);
  }

  @Override
  public void setOutputError()
  {
    setError(PORT_Y);
  }

  @Override
  public void setOutputUnsettled()
  {
  }

  @Override
  public void setOutput(long outputValue)
  {
    setValue(PORT_Y, outputValue);
  }
}

