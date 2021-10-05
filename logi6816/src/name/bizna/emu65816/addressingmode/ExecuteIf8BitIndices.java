package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.function.Consumer;

public class ExecuteIf8BitIndices
    extends Operation
{
  private final Consumer<Cpu65816> function;

  public ExecuteIf8BitIndices(Consumer<Cpu65816> function)
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
}

