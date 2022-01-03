package net.integratedcircuits.nexperia.hct193;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface HCT193Pins
    extends Pins<HCT193Snapshot, HCT193Pins, HCT193>
{
  BusValue getInput();

  void setOutput(long latchValue);

  PinValue getCPD();

  PinValue getCPU();

  PinValue getMR();

  PinValue getPLB();

  void setTCUB(boolean upCarryValue);

  void setTCDB(boolean downBorrowValue);
}

