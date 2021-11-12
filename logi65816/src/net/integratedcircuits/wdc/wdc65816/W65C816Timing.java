package net.integratedcircuits.wdc.wdc65816;

public class W65C816Timing
{
  public TimeRange addressOut;
  public TimeRange bankOut;
  public TimeRange writeDataOut;
  public TimeRange readDataRequired;
  public TimeRange mOut;
  public TimeRange xOut;

  public boolean notConnected;

  public W65C816Timing()
  {
    this.addressOut = new TimeRange();
    this.bankOut = new TimeRange();
    this.writeDataOut = new TimeRange();
    this.readDataRequired = new TimeRange();
    this.mOut = new TimeRange();
    this.xOut = new TimeRange();

    notConnected = true;
  }



  public void set(int addressOutStart,
                  int addressOutStop,
                  int bankOutStart,
                  int bankOutStop,
                  int writeDataOutStart,
                  int writeDataOutStop,
                  int readDataRequiredStart,
                  int readDataRequiredStop,
                  int mOutStart,
                  int mOutStop,
                  int xOutStart,
                  int xOutStop)
  {
    notConnected = false;

    this.addressOut.set(addressOutStart, addressOutStop);
    this.bankOut.set(bankOutStart, bankOutStop);
    this.writeDataOut.set(writeDataOutStart, writeDataOutStop);
    this.readDataRequired.set(readDataRequiredStart, readDataRequiredStop);
    this.mOut.set(mOutStart, mOutStop);
    this.xOut.set(xOutStart, xOutStop);
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
        get4Bit(11, timingValue));
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

