package net.integratedcircuits.wdc.wdc65816;

import net.common.BusValue;
import net.common.Pins;

public interface W65C816Pins
    extends Pins<W65C816Snapshot, W65C816Pins, W65C816>
{
  void setAddress(int address);

  int getData();

  int peekData();

  void setData(int data);

  void setBank(int data);

  void setRWB(boolean rwB);

  void setEmulation(boolean emulation);

  void setMemoryLockB(boolean memoryLock);

  void setMX(boolean m);

  void setRdy(boolean rdy);

  void setVectorPullB(boolean vectorPullB);

  void setValidProgramAddress(boolean validProgramAddress);

  void setValidDataAddress(boolean validDataAddress);

  boolean isBusEnable();

  boolean isReset();

  boolean isIRQ();

  boolean isNMI();

  boolean isAbort();

  void disableBusses();

  boolean isClock();

  long getTimingValue();

  boolean isTimingClock();
}

