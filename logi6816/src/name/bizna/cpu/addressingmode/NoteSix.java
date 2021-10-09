package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Address;
import name.bizna.cpu.Cpu65816;

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
    if (cpu.isEmulation())
    {
      int pcOffset = cpu.getProgramCounter().getOffset();
      return Address.areOffsetsAreOnDifferentPages(pcOffset, pcOffset + cpu.getData16Bit());
    }
    return false;
  }

  @Override
  public String toString()
  {
    return "Note(6)";
  }
}

