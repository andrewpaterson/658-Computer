package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;

public class NoteSix
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    if (cpu.isEmulationMode())
    {
      int pcOffset = cpu.getProgramCounter().getOffset();
      return Address.areOffsetsAreOnDifferentPages(pcOffset, pcOffset + cpu.getData16Bit());
    }
    return false;
  }
}

