package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import static name.bizna.cpu.Executor.getMethodName;

public class ExecuteIf8BitIndices
    extends Operation
{
  private final Executor<Cpu65816> function;

  public ExecuteIf8BitIndices(Executor<Cpu65816> function)
  {
    this.function = function;
  }

  @Override
  public void execute(Cpu65816 cpu)
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

