package net.integratedcircuits.ti.hct40103;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface HCT40103Pins
    extends Pins<HCT40103Snapshot, HCT40103Pins, HCT40103>
{
  BusValue getInput();

  void setOutput(long latchValue);

  PinValue getCP();

  PinValue getMRB();

  PinValue getPLB();

  PinValue getPEB();

  PinValue getTEB();

  void setTCB(boolean downBorrowValue);
}

