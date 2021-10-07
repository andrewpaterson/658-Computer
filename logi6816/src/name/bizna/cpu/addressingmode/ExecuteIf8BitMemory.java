package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import java.util.function.Consumer;

public class ExecuteIf8BitMemory
    extends Operation
{
  private final Consumer<Cpu65816> function;

  public ExecuteIf8BitMemory(Consumer<Cpu65816> function)
  {
    this.function = function;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (cpu.isMemory8Bit())
    {
      function.accept(cpu);
    }
  }

  @Override
  public String toString()
  {
    return "Execute_If_8Bit_M_" + function;
  }
}

