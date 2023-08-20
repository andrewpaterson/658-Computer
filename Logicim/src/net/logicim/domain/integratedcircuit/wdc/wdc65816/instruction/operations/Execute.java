package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.Executor;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

import static net.logicim.domain.integratedcircuit.wdc.wdc65816.Executor.getMethodName;

public class Execute
    extends Operation
{
  private final Executor<W65C816> function;

  public Execute(Executor<W65C816> function)
  {
    this.function = function;
  }

  @Override
  public void execute(W65C816 cpu)
  {
    function.accept(cpu);
  }

  @Override
  public String toString()
  {
    return "Execute(" + getMethodName(function) + ")";
  }
}

