package name.bizna.emu65816;

public interface Pins
{
  int getAddress();

  void setAddress(int address);

  int getData();

  void setData(int data);

  void setRead(boolean read);

  boolean isRead();

  boolean getPhi2();

  void setPhi2(boolean phi2);

  void setEmulation(boolean emulation);

  void setMemoryLockB(boolean memoryLock);

  void setMX(boolean mx);

  void setReady(boolean ready);

  void setVectorPullB(boolean vectorPullB);

  boolean isValidProgramAddress();

  void setValidProgramAddress(boolean validProgramAddress);

  boolean isValidDataAddress();

  void setValidDataAddress(boolean validDataAddress);

  boolean isEmulation();

  boolean isMemoryLockB();

  boolean isReady();

  boolean isVectorPullB();

  boolean isBusEnable();

  boolean isPhi2();

  boolean isIrqB();

  boolean isNmiB();

  boolean isMX();

  boolean isResetB();

  boolean isAbortB();
}

