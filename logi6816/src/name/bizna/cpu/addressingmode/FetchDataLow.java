package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

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
    cpu.setDataLow(cpu.getPins().getData());
  }

  @Override
  public String toString()
  {
    return "Read(DL)";
  }
}

