package net.integratedcircuits.wdc.wdc65816;

public class W65C816Timing
{
  public TimeRange addressOut;
  public TimeRange bankOut;
  public TimeRange writeDataOut;
  public TimeRange readDataAndIntRequired;
  public TimeRange mOut;
  public TimeRange xOut;
  public TimeRange eOut;
  public TimeRange readAbortRequired;

  public boolean notConnected;

  public W65C816Timing()
  {
    this.addressOut = new TimeRange();
    this.bankOut = new TimeRange();
    this.writeDataOut = new TimeRange();
    this.readDataAndIntRequired = new TimeRange();
    this.mOut = new TimeRange();
    this.xOut = new TimeRange();
    this.eOut = new TimeRange();
    this.readAbortRequired = new TimeRange();

    notConnected = true;
  }

  public void set(int addressOutStart,
                  int addressOutStop,
                  int bankOutStart,
                  int bankOutStop,
                  int writeDataOutStart,
                  int writeDataOutStop,
                  int readDataAndIntRequiredStart,
                  int readDataAndIntRequiredStop,
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

    this.addressOut.set(addressOutStart, addressOutStop);
    this.bankOut.set(bankOutStart, bankOutStop);
    this.writeDataOut.set(writeDataOutStart, writeDataOutStop);
    this.readDataAndIntRequired.set(readDataAndIntRequiredStart, readDataAndIntRequiredStop);
    this.mOut.set(mOutStart, mOutStop);
    this.xOut.set(xOutStart, xOutStop);
    this.eOut.set(eOutStart, eOutStop);
    this.readAbortRequired.set(readAbortRequiredStart, readAbortRequiredStop);
  }

  public void setFromLong(long timingValue)
  {
    set(get4Bit(0, timingValue),
        get4Bit(1, timingValue),
        get4Bit(2, timingValue),
        get4Bit(3, timingValue),
        get4Bit(4, timingValue),
        get4Bit(5, timingValue),
        get4Bit(6, timingValue),
        get4Bit(7, timingValue),
        get4Bit(8, timingValue),
        get4Bit(9, timingValue),
        get4Bit(10, timingValue),
        get4Bit(11, timingValue),
        get4Bit(12, timingValue),
        get4Bit(13, timingValue),
        get4Bit(14, timingValue),
        get4Bit(15, timingValue));
  }

  private int get4Bit(int index, long value)
  {
    int shift = index * 4;
    value = value >> shift;
    return (int) (value & 0xf);
  }

  public void setNotConnected()
  {
    notConnected = true;
  }
}

