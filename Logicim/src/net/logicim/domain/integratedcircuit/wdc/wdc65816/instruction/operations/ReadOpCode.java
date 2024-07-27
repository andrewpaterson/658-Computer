package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class ReadOpCode
    extends DataOperation
{
  public ReadOpCode()
  {
    super(true, true, true, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.setOpCode(cpu.getData());
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

