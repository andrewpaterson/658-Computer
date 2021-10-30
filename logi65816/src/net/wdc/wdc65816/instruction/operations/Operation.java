package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.W65C816;
import net.wdc.wdc65816.instruction.BusCycleParameter;

public abstract class Operation
    implements BusCycleParameter
{
  public abstract void execute(W65C816 cpu);

  @Override
  public boolean isOperation()
  {
    return true;
  }

  public boolean mustExecute(W65C816 cpu)
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

