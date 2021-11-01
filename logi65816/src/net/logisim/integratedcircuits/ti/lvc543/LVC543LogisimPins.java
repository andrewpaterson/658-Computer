package net.logisim.integratedcircuits.ti.lvc543;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.ti.lvc543.LVC543;
import net.integratedcircuits.ti.lvc543.LVC543Pins;
import net.integratedcircuits.ti.lvc543.LVC543Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.ti.lvc543.LVC543Factory.*;

public class LVC543LogisimPins
    extends LogisimPins<LVC543Snapshot, LVC543Pins, LVC543>
    implements LVC543Pins
{
  @Override
  public PinValue getLEB(int index)
  {
    return getValue(PORT_LEB[index]);
  }

  @Override
  public PinValue getCEB(int index)
  {
    return getValue(PORT_CEB[index]);
  }

  @Override
  public BusValue getInput(int index)
  {
    return getValue(PORT_IO[index]);
  }

  @Override
  public void setOutputUnsettled(int index)
  {
  }

  @Override
  public void setOutputError(int index)
  {
    setError(PORT_IO[index]);
  }

  @Override
  public void setOutput(int index, long latchValue)
  {
    setValue(PORT_IO[index], latchValue);
  }

  @Override
  public PinValue getOEB(int index)
  {
    return getValue(PORT_OEB[index]);
  }

  @Override
  public void setOutputHighImpedance(int index)
  {
    setHighImpedance(PORT_IO[index]);
  }
}

