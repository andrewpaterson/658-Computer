package net.logisim.integratedcircuits.ti.hc147;

import net.common.BusValue;
import net.integratedcircuits.ti.hc147.HC147;
import net.integratedcircuits.ti.hc147.HC147Pins;
import net.integratedcircuits.ti.hc147.HC147Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.ti.hc147.HC147Factory.*;

public class HC147LogisimPins
    extends LogisimPins<HC147Snapshot, HC147Pins, HC147>
    implements HC147Pins
{
  @Override
  public BusValue getInput()
  {
    return getValue(PORT_INPUT);
  }

  @Override
  public void setOutputError()
  {
    setError(PORT_OUTPUT);
  }

  @Override
  public void setOutput(long value)
  {
    setValue(PORT_OUTPUT, value);
  }

  @Override
  public void setOutputUnsettled()
  {
  }
}

