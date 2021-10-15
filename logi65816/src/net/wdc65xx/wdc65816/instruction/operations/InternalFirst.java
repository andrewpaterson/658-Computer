package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class InternalFirst
    extends DataOperation
{
  public InternalFirst()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "OpCode";
  }
}

