package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

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

