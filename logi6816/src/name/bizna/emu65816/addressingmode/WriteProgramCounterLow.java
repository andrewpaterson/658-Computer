package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

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
    cpu.getPins().setData(Binary.getLowByte(cpu.getProgramCounter().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(PCL)";
  }
}

