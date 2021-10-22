package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65C816;

public class ReadOpCode
    extends DataOperation
{
  public ReadOpCode()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.setOpCode(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "OpCode";
  }

  public int getDone8()
  {
    return 1;
  }

  public int getDone16()
  {
    return 1;
  }

  public boolean isFetchOpCode()
  {
    return true;
  }
}

