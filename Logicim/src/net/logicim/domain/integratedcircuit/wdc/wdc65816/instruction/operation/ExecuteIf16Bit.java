package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.common.SimulatorException;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.Executor;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.WidthFromRegister;

import static net.logicim.domain.integratedcircuit.wdc.wdc65816.Executor.getMethodName;
import static net.logicim.domain.integratedcircuit.wdc.wdc65816.WidthFromRegister.M;
import static net.logicim.domain.integratedcircuit.wdc.wdc65816.WidthFromRegister.XY;

public class ExecuteIf16Bit
    extends Operation
{
  private Executor<W65C816> function;
  private WidthFromRegister width;

  public ExecuteIf16Bit(Executor<W65C816> function, WidthFromRegister width)
  {
    this.function = function;
    this.width = width;
  }

  @Override
  public void execute(W65C816 cpu)
  {
    if (width == M)
    {
      if (cpu.getState().isMemory16Bit())
      {
        function.accept(cpu);
      }
    }
    else if (width == XY)
    {
      if (cpu.getState().isIndex16Bit())
      {
        function.accept(cpu);
      }
    }
    else
    {
      throw new SimulatorException("Unknown Width.");
    }
  }

  @Override
  public String toString()
  {
    return "Execute_If_16Bit_" + width + "(" + getMethodName(function) + ")";
  }
}

