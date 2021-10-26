package net.wdc.wdc65816;

import net.common.Pins;

public interface WDC65816Pins
    extends Pins<WDC65816Snapshot, WDC65816Pins, WDC65816>
{
  void setAddress(int address);

  int getData();

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
}

