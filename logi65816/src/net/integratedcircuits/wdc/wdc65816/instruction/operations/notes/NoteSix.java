package net.integratedcircuits.wdc.wdc65816.instruction.operations.notes;

import net.integratedcircuits.wdc.wdc65816.Address;
import net.integratedcircuits.wdc.wdc65816.W65C816;
import net.integratedcircuits.wdc.wdc65816.instruction.operations.Operation;

public class NoteSix
    extends Operation
{
  @Override
  public void execute(W65C816 cpu)
  {
  }

  @Override
  public boolean mustExecute(W65C816 cpu)
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

