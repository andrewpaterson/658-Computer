package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class WaitOperation
    extends DataOperation
{
  public WaitOperation()
  {
    super(false, false, true, true, true);
    ready = false;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "Wait";
  }
}

