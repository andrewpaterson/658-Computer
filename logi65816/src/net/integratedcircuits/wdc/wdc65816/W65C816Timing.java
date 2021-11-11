package net.integratedcircuits.wdc.wdc65816;

public class W65C816Timing
{
  public int addressOutStart;
  public int addressOutStop;  //Inclusive

  public int bankOutStart;
  public int bankOutStop;  //Inclusive

  public int writeDataOutStart;
  public int writeDataOutStop;  //Inclusive

  public int readDataRequiredStart;
  public int readDataRequiredStop;  //Inclusive

  public int mOutStart;
  public int mOutStop;  //Inclusive

  public int xOutStart;
  public int xOutStop;  //Inclusive

  public boolean notConnected;

  public W65C816Timing()
  {
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

    this.addressOutStart = addressOutStart;
    this.addressOutStop = addressOutStop;
    this.bankOutStart = bankOutStart;
    this.bankOutStop = bankOutStop;
    this.writeDataOutStart = writeDataOutStart;
    this.writeDataOutStop = writeDataOutStop;
    this.readDataRequiredStart = readDataRequiredStart;
    this.readDataRequiredStop = readDataRequiredStop;
    this.mOutStart = mOutStart;
    this.mOutStop = mOutStop;
    this.xOutStart = xOutStart;
    this.xOutStop = xOutStop;
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
    int shift =  index * 4;
    value = value >> shift;
    return (int) (value & 0xf);
  }

  public void setNotConnected()
  {
    notConnected = true;
  }
}

