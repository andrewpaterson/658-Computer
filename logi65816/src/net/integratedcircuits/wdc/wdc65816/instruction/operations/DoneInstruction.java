package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class DoneInstruction
    extends Operation
{
  @Override
  public void execute(W65C816 cpu)
  {
    cpu.doneInstruction();
  }

  @Override
  public String toString()
  {
    return "DONE";
  }

  public int getDone8()
  {
    return 1;
  }

  public int getDone16()
  {
    return 1;
  }
}

