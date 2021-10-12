package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

public class DoneInstruction
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.doneInstruction();
  }

  @Override
  public String toString()
  {
    return "DONE";
  }

  @Override
  public boolean isDone()
  {
    return true;
  }
}

