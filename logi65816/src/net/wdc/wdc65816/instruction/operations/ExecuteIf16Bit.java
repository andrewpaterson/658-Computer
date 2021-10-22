package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.Executor;
import net.wdc.wdc65816.WidthFromRegister;
import net.util.EmulatorException;

import static net.wdc.wdc65816.Executor.getMethodName;
import static net.wdc.wdc65816.WidthFromRegister.M;
import static net.wdc.wdc65816.WidthFromRegister.XY;

public class ExecuteIf16Bit
    extends Operation
{
  private final Executor<WDC65816> function;
  private final WidthFromRegister width;

  public ExecuteIf16Bit(Executor<WDC65816> function, WidthFromRegister width)
  {
    this.function = function;
    this.width = width;
  }

  @Override
  public void execute(WDC65816 cpu)
  {
    if (width == M)
    {
      if (cpu.isMemory16Bit())
      {
        function.accept(cpu);
      }
    }
    else if (width == XY)
    {
      if (cpu.isIndex16Bit())
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
    return "Execute_If_16Bit_" + width + "(" + getMethodName(function) + ")";
  }
}

