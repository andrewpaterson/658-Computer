package net.integratedcircuits.wdc.wdc65816;

public class W65C816Timing
{
  public int executeOperationInWriteCycle;
  public int executeOperationInReadCycle;
  public TimeRange addressOut;
  public TimeRange bankOut;
  public TimeRange writeDataOut;
  public TimeRange readDataRequired;
  public TimeRange readInterruptsRequired;
  public TimeRange readAbortRequired;
  public TimeRange mOut;
  public TimeRange xOut;
  public TimeRange eOut;

  public boolean notConnected;

  public W65C816Timing()
  {
    this.executeOperationInReadCycle = 0;
    this.executeOperationInWriteCycle = 0;
    this.addressOut = new TimeRange();
    this.bankOut = new TimeRange();
    this.writeDataOut = new TimeRange();
    this.readDataRequired = new TimeRange();
    this.readInterruptsRequired = new TimeRange();
    this.mOut = new TimeRange();
    this.xOut = new TimeRange();
    this.eOut = new TimeRange();
    this.readAbortRequired = new TimeRange();

    notConnected = true;
  }

  public void set(int executeOperationInReadCycle,
                  int executeOperationInWriteCycle,
                  int addressOutStart,
                  int addressOutStop,
                  int bankOutStart,
                  int bankOutStop,
                  int writeDataOutStart,
                  int writeDataOutStop,
                  int readDataRequiredStart,
                  int readDataRequiredStop,
                  int readInterruptsRequiredStart,
                  int readInterruptsRequiredStop,
                  int mOutStart,
                  int mOutStop,
                  int xOutStart,
                  int xOutStop,
                  int eOutStart,
                  int eOutStop,
                  int readAbortRequiredStart,
                  int readAbortRequiredStop)
  {
    notConnected = false;

    this.executeOperationInReadCycle = executeOperationInReadCycle;
    this.executeOperationInWriteCycle = executeOperationInWriteCycle;
    this.addressOut.set(addressOutStart, addressOutStop);
    this.bankOut.set(bankOutStart, bankOutStop);
    this.writeDataOut.set(writeDataOutStart, writeDataOutStop);
    this.readDataRequired.set(readDataRequiredStart, readDataRequiredStop);
    this.readInterruptsRequired.set(readInterruptsRequiredStart, readInterruptsRequiredStop);
    this.mOut.set(mOutStart, mOutStop);
    this.xOut.set(xOutStart, xOutStop);
    this.eOut.set(eOutStart, eOutStop);
    this.readAbortRequired.set(readAbortRequiredStart, readAbortRequiredStop);
  }

  public void setFromLong(long timing1Value, long timing2Value)
  {
    set(get4Bit(0, timing1Value),
        get4Bit(1, timing1Value),
        get4Bit(2, timing1Value),
        get4Bit(3, timing1Value),
        get4Bit(4, timing1Value),
        get4Bit(5, timing1Value),
        get4Bit(6, timing1Value),
        get4Bit(7, timing1Value),
        get4Bit(8, timing1Value),
        get4Bit(9, timing1Value),

        get4Bit(0, timing2Value),
        get4Bit(1, timing2Value),
        get4Bit(2, timing2Value),
        get4Bit(3, timing2Value),
        get4Bit(4, timing2Value),
        get4Bit(5, timing2Value),
        get4Bit(6, timing2Value),
        get4Bit(7, timing2Value),
        get4Bit(8, timing2Value),
        get4Bit(9, timing2Value));
  }

  private int get4Bit(int index, long value)
  {
    int shift = index * 4;
    value = value >> shift;
    return (int) (value & 0xf);
  }

  private long set4Bit(int index, long value)
  {
    int shift = index * 4;
    return value << shift;
  }

  public void setNotConnected()
  {
    notConnected = true;
  }

  public long getT1()
  {
    return set4Bit(0, executeOperationInReadCycle) |
           set4Bit(1, executeOperationInWriteCycle) |
           set4Bit(2, addressOut.start) |
           set4Bit(3, addressOut.stop) |
           set4Bit(4, bankOut.start) |
           set4Bit(5, bankOut.stop) |
           set4Bit(6, writeDataOut.start) |
           set4Bit(7, writeDataOut.stop) |
           set4Bit(8, readDataRequired.start) |
           set4Bit(9, readDataRequired.stop);
  }

  public long getT2()
  {
    return set4Bit(0, readInterruptsRequired.start) |
           set4Bit(1, readInterruptsRequired.stop) |
           set4Bit(2, mOut.start) |
           set4Bit(3, mOut.stop) |
           set4Bit(4, xOut.start) |
           set4Bit(5, xOut.stop) |
           set4Bit(6, eOut.start) |
           set4Bit(7, eOut.stop) |
           set4Bit(8, readAbortRequired.start) |
           set4Bit(9, readAbortRequired.stop);
  }
}

