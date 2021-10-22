package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.Executor;

import static net.wdc.wdc65816.Executor.getMethodName;

public class ExecuteIf8BitIndices
    extends Operation
{
  private final Executor<WDC65816> function;

  public ExecuteIf8BitIndices(Executor<WDC65816> function)
  {
    this.function = function;
  }

  @Override
  public void execute(WDC65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      function.accept(cpu);
    }
  }

  @Override
  public String toString()
  {
    return "Execute_If_8Bit_XY(" + getMethodName(function) + ")";
  }
}

