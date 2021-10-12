package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

public class DecrementStackPointer
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.decrementStackPointer();
  }

  @Override
  public String toString()
  {
    return "SP--";
  }
}

