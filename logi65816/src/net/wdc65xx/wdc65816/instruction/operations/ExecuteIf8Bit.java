package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;
import net.wdc65xx.wdc65816.WidthFromRegister;
import net.util.EmulatorException;

import static net.wdc65xx.wdc65816.Executor.getMethodName;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;
import static net.wdc65xx.wdc65816.WidthFromRegister.XY;

public class ExecuteIf8Bit
    extends Operation
{
  private final Executor<Cpu65816> function;
  private final WidthFromRegister width;

  public ExecuteIf8Bit(Executor<Cpu65816> function, WidthFromRegister width)
  {
    this.function = function;
    this.width = width;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (width == M)
    {
      if (cpu.isMemory8Bit())
      {
        function.accept(cpu);
      }
    }
    else if (width == XY)
    {
      if (cpu.isIndex8Bit())
      {
        function.accept(cpu);
      }
    }
    else
    {
      throw new EmulatorException("Unknown Width.");
    }
  }

  @Override
  public String toString()
  {
    return "Execute_If_8Bit_" + width + "(" + getMethodName(function) + ")";
  }
}
