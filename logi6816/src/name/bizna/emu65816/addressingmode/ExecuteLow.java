package name.bizna.emu65816.addressingmode;

public class ReadDataLow
    extends Data
{
  public ReadDataLow(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }
}

