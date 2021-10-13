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

  public int getDone8()
  {
    return 1;
  }

  public int getDone16()
  {
    return 1;
  }
}

