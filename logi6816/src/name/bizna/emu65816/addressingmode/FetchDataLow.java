package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class FetchDataLow
    extends DataOperation
{
  public FetchDataLow(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setDataLow(cpu.getPinData());
  }

  @Override
  public String toString()
  {
    return "Read(DL)";
  }
}

