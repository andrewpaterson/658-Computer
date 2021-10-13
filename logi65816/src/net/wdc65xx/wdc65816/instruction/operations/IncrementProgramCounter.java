package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class IncrementProgramCounter
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.incrementProgramAddress();
  }

  @Override
  public String toString()
  {
    return "PC++";
  }
}

