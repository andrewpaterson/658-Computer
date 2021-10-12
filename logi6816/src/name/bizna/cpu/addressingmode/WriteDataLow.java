package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class WriteDataLow
    extends DataOperation
{
  public WriteDataLow(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, false, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getPins().setData(cpu.getDataLow());
  }

  @Override
  public String toString()
  {
    return "Write(DL)";
  }
}

