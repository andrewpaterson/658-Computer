package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class IncrementProgramCounter
    extends Operation
{
  @Override
  public void execute(W65C816 cpu)
  {
    cpu.incrementProgramAddress();
  }

  @Override
  public String toString()
  {
    return "PC++";
  }
}

