package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class WaitOperation
    extends DataOperation
{
  public WaitOperation()
  {
    super(false, false, true, true, true);
    ready = false;
  }

  @Override
  public void execute(W65C816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "Wait";
  }
}

