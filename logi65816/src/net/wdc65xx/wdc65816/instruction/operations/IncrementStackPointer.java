package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class IncrementStackPointer
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.incrementStackPointer();
  }

  @Override
  public String toString()
  {
    return "SP++";
  }
}

