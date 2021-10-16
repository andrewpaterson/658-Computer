package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.WDC65C816;

public class WaitOperation
    extends DataOperation
{
  public WaitOperation()
  {
    super(false, false, true, true, true);
    ready = false;
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
  }

  @Override
  public String toString()
  {
    return "Wait";
  }
}

