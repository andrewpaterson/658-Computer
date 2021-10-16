package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.WDC65C816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.Executor.getMethodName;

public class Execute
    extends Operation
{
  private final Executor<WDC65C816> function;

  public Execute(Executor<WDC65C816> function)
  {
    this.function = function;
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    function.accept(cpu);
  }

  @Override
  public String toString()
  {
    return "Execute(" + getMethodName(function) + ")";
  }
}

