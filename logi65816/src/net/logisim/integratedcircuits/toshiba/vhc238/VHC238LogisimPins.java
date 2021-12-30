package net.logisim.integratedcircuits.toshiba.vhc238;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.toshiba.vhc238.VHC238;
import net.integratedcircuits.toshiba.vhc238.VHC238Pins;
import net.integratedcircuits.toshiba.vhc238.VHC238Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.toshiba.vhc238.VHC238Factory.*;

public class VHC238LogisimPins
    extends LogisimPins<VHC238Snapshot, VHC238Pins, VHC238>
    implements VHC238Pins
{
  @Override
  public PinValue getE1B()
  {
    return getValue(PORT_G2A);
  }

  @Override
  public PinValue getE2B()
  {
    return getValue(PORT_G2B);
  }

  @Override
  public PinValue getE3()
  {
    return getValue(PORT_G1);
  }

  @Override
  public BusValue getA()
  {
    return getValue(PORT_ABC);
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

