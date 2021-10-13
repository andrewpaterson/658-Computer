package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class DecrementProgramCounter
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.decrementProgramCounter();
  }

  @Override
  public String toString()
  {
    return "PC--";
  }
}

