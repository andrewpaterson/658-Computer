package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

public abstract class Operation
    implements BusCycleParameter
{
  public abstract void execute(Cpu65816 cpu);

  @Override
  public boolean isOperation()
  {
    return true;
  }

  public boolean mustExecute(Cpu65816 cpu)
  {
    return true;
  }

  @Override
  public boolean isAddress()
  {
    return false;
  }

  public boolean isData()
  {
    return false;
  }

  public int getDone8()
  {
    return 0;
  }

  public int getDone16()
  {
    return 0;
  }
}

