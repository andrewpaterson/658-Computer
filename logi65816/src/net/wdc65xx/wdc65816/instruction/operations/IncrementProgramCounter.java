package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.WDC65C816;

public class IncrementProgramCounter
    extends Operation
{
  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.incrementProgramAddress();
  }

  @Override
  public String toString()
  {
    return "PC++";
  }
}

