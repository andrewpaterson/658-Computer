package net.logisim.integratedcircuits.ti.hct40103;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.ti.hct40103.HCT40103;
import net.integratedcircuits.ti.hct40103.HCT40103Pins;
import net.integratedcircuits.ti.hct40103.HCT40103Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.ti.hct40103.HCT40103Factory.*;

public class HCT40103LogisimPins
    extends LogisimPins<HCT40103Snapshot, HCT40103Pins, HCT40103>
    implements HCT40103Pins
{
  @Override
  public BusValue getInput()
  {
    return getValue(PORT_P);
  }

  @Override
  public void setOutput(long latchValue)
  {
    setValue(PORT_P, latchValue);
  }

  @Override
  public PinValue getCP()
  {
    return getValue(PORT_CP);
  }

  @Override
  public PinValue getMRB()
  {
    return getValue(PORT_MRB);
  }

  @Override
  public PinValue getPLB()
  {
    return getValue(PORT_PLB);
  }

  @Override
  public PinValue getPEB()
  {
    return getValue(PORT_PEB);
  }

  @Override
  public PinValue getTEB()
  {
    return getValue(PORT_TE);
  }

  @Override
  public void setTCB(boolean downBorrowValue)
  {
    setValue(PORT_TCB, downBorrowValue);
  }
}

