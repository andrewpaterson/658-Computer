package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.Executor;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

import static net.logicim.domain.integratedcircuit.wdc.wdc65816.Executor.getMethodName;

public class ExecuteIf8BitIndices
    extends Operation
{
  private final Executor<W65C816> function;

  public ExecuteIf8BitIndices(Executor<W65C816> function)
  {
    this.function = function;
  }

  @Override
  public void execute(W65C816 cpu)
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

