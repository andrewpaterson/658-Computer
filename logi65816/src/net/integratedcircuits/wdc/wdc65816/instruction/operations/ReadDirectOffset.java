package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class ReadDirectOffset
    extends DataOperation
{
  public ReadDirectOffset(boolean notMemoryLock)
  {
    super(true, false, notMemoryLock, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.setDirectOffset(cpu.readPinData());
  }

  @Override
  public String toString()
  {
    return "Read(D0)";
  }
}

