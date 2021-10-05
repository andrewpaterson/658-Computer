package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

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
}

