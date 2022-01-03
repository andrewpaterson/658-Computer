package net.logisim.integratedcircuits.nexperia.hct193;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.hct193.HCT193;
import net.integratedcircuits.nexperia.hct193.HCT193Pins;
import net.integratedcircuits.nexperia.hct193.HCT193Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.hct193.HCT193Factory.*;

public class HCT193LogisimPins
    extends LogisimPins<HCT193Snapshot, HCT193Pins, HCT193>
    implements HCT193Pins
{
  @Override
  public BusValue getInput()
  {
    return getValue(PORT_D);
  }

  @Override
  public void setOutput(long latchValue)
  {
    setValue(PORT_Q, latchValue);
  }

  @Override
  public PinValue getCPD()
  {
    return getValue(PORT_CPD);
  }

  @Override
  public PinValue getCPU()
  {
    return getValue(PORT_CPU);
  }

  @Override
  public PinValue getMR()
  {
    return getValue(PORT_MR);
  }

  @Override
  public PinValue getPLB()
  {
    return getValue(PORT_PLB);
  }

  @Override
  public void setTCUB(boolean upCarryValue)
  {
    setValue(PORT_TCUB, upCarryValue);
  }

  @Override
  public void setTCDB(boolean downBorrowValue)
  {
    setValue(PORT_TCDB, downBorrowValue);
  }
}

