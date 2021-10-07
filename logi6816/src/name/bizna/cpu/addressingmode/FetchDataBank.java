package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class FetchDataBank
    extends DataOperation
{
  public FetchDataBank(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setDataBank(cpu.getPinData());
  }


  @Override
  public String toString()
  {
    return "Read(DBR)";
  }
}

