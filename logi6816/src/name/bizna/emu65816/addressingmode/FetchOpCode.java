package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class FetchOpCode
    extends DataOperation
{
  public FetchOpCode()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setOpCode(cpu.getPinData());
  }
}

