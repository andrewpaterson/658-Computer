package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

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
}

