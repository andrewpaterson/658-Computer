package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

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

  @Override
  public String toString()
  {
    return "Write(PBR)";
  }
}

