package net.logisim.integratedcircuits.toshiba.vhc161;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.toshiba.vhc161.VHC161;
import net.integratedcircuits.toshiba.vhc161.VHC161Pins;
import net.integratedcircuits.toshiba.vhc161.VHC161Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.toshiba.vhc161.VHC161Factory.*;

public class VHC161LogisimPins
    extends LogisimPins<VHC161Snapshot, VHC161Pins, VHC161>
    implements VHC161Pins
{
  @Override
  public BusValue getInput()
  {
    return getValue(VHC161Factory.PORT_ABCD);
  }

  @Override
  public void setOutputUnsettled()
  {
  }

  @Override
  public void setOutputError()
  {
    setError(PORT_QABCD);
  }

  @Override
  public void setOutput(long latchValue)
  {
    setValue(PORT_QABCD, latchValue);
  }

  @Override
  public PinValue getParallelLoadB()
  {
    return getValue(PORT_LOAD);
  }

  @Override
  public PinValue getMasterResetB()
  {
    return getValue(PORT_CLR);
  }

  @Override
  public PinValue getClock()
  {
    return getValue(PORT_CK);
  }

  @Override
  public PinValue getCountEnabled()
  {
    return getValue(PORT_ENP);
  }

  @Override
  public PinValue getCET()
  {
    return getValue(PORT_ENT);
  }

  @Override
  public void setCarry(boolean value)
  {
    setValue(PORT_CO, value);
  }
}

