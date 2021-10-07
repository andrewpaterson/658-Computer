package name.bizna.cpu;

public interface Pins65816
{
  void setAddress(int address);

  int getData();

  void setData(int data);

  void setRwb(boolean rwb);

  boolean getPhi2();

  void setEmulation(boolean emulation);

  void setMemoryLockB(boolean memoryLock);

  void setMX(boolean mx);

  void setRdy(boolean rdy);

  void setVectorPullB(boolean vectorPullB);

  void setValidProgramAddress(boolean validProgramAddress);

  void setValidDataAddress(boolean validDataAddress);

  boolean isBusEnable();

  boolean isPhi2();

  boolean isIrqB();

  boolean isNmiB();

  boolean isAbortB();

  boolean isResetB();
}

