package net.integratedcircuits.wdc.wdc65816;

import net.common.Pins;

public interface W65C816Pins
    extends Pins<W65C816Snapshot, W65C816Pins, W65C816>
{
  void setAddress(int address);

  void setAddressUnknown();

  int getData();

  int peekData();

  void setData(int data);

  void setDataUnknown();

  void setBank(int data);

  void setRWB(boolean rwB);

  void setRWBUnknown();

  void setEmulation(boolean emulation);

  void setEmulationUnknown();

  void setMemoryLockB(boolean memoryLock);

  void setMemoryLockBUnknown();

  void setMX(boolean m);

  void setMXUnknown();

  void setRdy(boolean rdy);

  void setRdyUnknown();

  void setVectorPullB(boolean vectorPullB);

  void setVectorPullBUnknown();

  void setValidProgramAddress(boolean validProgramAddress);

  void setValidProgramAddressUnknown();

  void setValidDataAddress(boolean validDataAddress);

  void setValidDataAddressUnknown();

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

