package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ExecuteOnCpu
    extends Operation
{
  private final Consumer<Cpu65816> function;

  public ExecuteOnCpu(Consumer<Cpu65816> function)
  {
    this.function = function;
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    function.accept(cpu);
  }
}

