package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.WDC65C816;

public class InternalFirst
    extends DataOperation
{
  public InternalFirst()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "OpCode";
  }
}

