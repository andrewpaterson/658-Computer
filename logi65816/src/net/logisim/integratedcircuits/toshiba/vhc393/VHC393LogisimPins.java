package net.logisim.integratedcircuits.toshiba.vhc393;

import net.common.PinValue;
import net.integratedcircuits.toshiba.vhc393.VHC393;
import net.integratedcircuits.toshiba.vhc393.VHC393Pins;
import net.integratedcircuits.toshiba.vhc393.VHC393Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.toshiba.vhc393.VHC393Factory.*;

public class VHC393LogisimPins
    extends LogisimPins<VHC393Snapshot, VHC393Pins, VHC393>
    implements VHC393Pins
{
  @Override
  public void setOutputUnsettled(int index)
  {
  }

  @Override
  public void setOutputError(int index)
  {
    setError(PORT_Q[index]);
  }

  @Override
  public void setOutput(int index, long counterValue)
  {
    setValue(PORT_Q[index], counterValue);
  }

  @Override
  public PinValue getClear(int index)
  {
    return getValue(PORT_CLR[index]);
  }

  @Override
  public PinValue getClockB(int index)
  {
    return getValue(PORT_CPB[index]);
  }
}

