package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public class InternalFirst
    extends DataOperation
{
  public InternalFirst()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(WDC65816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "OpCode";
  }
}
