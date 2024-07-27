package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class InternalFirst
    extends DataOperation
{
  public InternalFirst()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "OpCode";
  }
}

