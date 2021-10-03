package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

import java.util.function.Consumer;

public class ExecuteIf16BitIndices
    extends Operation
{
  private final Consumer<Cpu65816> function;

  public ExecuteIf16BitIndices(Consumer<Cpu65816> function)
  {
    this.function = function;
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    if (cpu.isIndex16Bit())
    {
      function.accept(cpu);
    }
  }
}

