package net.wdc65xx.wdc65816;

public interface Pins65816
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

  boolean isIrqB();

  boolean isNmiB();

  boolean isAbortB();

  void setCpu(WDC65C816 cpu);
}

