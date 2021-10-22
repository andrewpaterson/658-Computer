package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public class WaitOperation
    extends DataOperation
{
  public WaitOperation()
  {
    super(false, false, true, true, true);
    ready = false;
  }

  @Override
  public void execute(WDC65816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "Wait";
  }
}

