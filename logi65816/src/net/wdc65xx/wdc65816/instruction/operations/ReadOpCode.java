package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class ReadOpCode
    extends DataOperation
{
  public ReadOpCode()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setOpCode(cpu.getPins().getData());
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

