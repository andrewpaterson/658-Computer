package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class WriteProgramBank
    extends DataOperation
{
  public WriteProgramBank()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getPins().setData(cpu.getProgramCounter().getBank());
  }
}

