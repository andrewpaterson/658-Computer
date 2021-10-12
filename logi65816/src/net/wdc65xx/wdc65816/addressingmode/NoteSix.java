package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Address;
import net.wdc65xx.wdc65816.Cpu65816;

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

