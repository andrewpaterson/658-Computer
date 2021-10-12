package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;
import name.bizna.cpu.WidthFromRegister;
import name.bizna.util.EmulatorException;

import static name.bizna.cpu.Executor.getMethodName;
import static name.bizna.cpu.WidthFromRegister.M;
import static name.bizna.cpu.WidthFromRegister.XY;

public class ExecuteIf16Bit
    extends Operation
{
  private final Executor<Cpu65816> function;
  private final WidthFromRegister width;

  public ExecuteIf16Bit(Executor<Cpu65816> function, WidthFromRegister width)
  {
    this.function = function;
    this.width = width;
  }

  @Override
  public void execute(Cpu65816 cpu)
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

