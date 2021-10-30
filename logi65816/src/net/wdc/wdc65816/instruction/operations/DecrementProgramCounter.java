package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.W65C816;

public class DecrementProgramCounter
    extends Operation
{
  @Override
  public void execute(W65C816 cpu)
  {
    cpu.decrementProgramCounter();
  }

  @Override
  public String toString()
  {
    return "PC--";
  }
}

