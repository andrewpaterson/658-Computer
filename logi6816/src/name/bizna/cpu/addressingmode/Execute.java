package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import java.util.function.Consumer;

public class Execute
    extends Operation
{
  private final Consumer<Cpu65816> function;

  public Execute(Consumer<Cpu65816> function)
  {
    this.function = function;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    function.accept(cpu);
  }

  @Override
  public String toString()
  {
    return "Execute_" + function;
  }
}

