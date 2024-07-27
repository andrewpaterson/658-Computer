package net.logicim.domain.integratedcircuit.wdc.wdc65816;

public interface W65C816Pins
{
  void setAddress(int address);

  int getData();

  int peekData();

  void setData(int data);

  void setDataUnknown();

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

  void disableBuses();

  boolean isClock();

  boolean isTimingClock();
}

