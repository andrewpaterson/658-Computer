package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.util.IntUtil;

public class WriteProgramCounterLow
    extends DataOperation
{
  public WriteProgramCounterLow()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getPins().setData(IntUtil.getLowByte(cpu.getProgramCounter().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(PCL)";
  }
}

