package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

public class NoteSeven
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    return !cpu.isEmulation();
  }

  @Override
  public String toString()
  {
    return "Note(7)";
  }
}

