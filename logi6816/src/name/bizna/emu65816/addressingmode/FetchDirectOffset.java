package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class FetchDirectOffset
    extends DataOperation
{
  public FetchDirectOffset(boolean notMemoryLock)
  {
    super(true, false, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setDirectOffset(cpu.getPinData());
  }


  @Override
  public String toString()
  {
    return "Read(D0)";
  }
}

