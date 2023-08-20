package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations;

import net.logicim.common.SimulatorException;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.WidthFromRegister;

import static net.logicim.domain.integratedcircuit.wdc.wdc65816.WidthFromRegister.M;
import static net.logicim.domain.integratedcircuit.wdc.wdc65816.WidthFromRegister.XY;

public class DoneInstructionIf8Bit
    extends Operation
{
  private final WidthFromRegister width;

  public DoneInstructionIf8Bit(WidthFromRegister width)
  {
    this.width = width;
  }

  @Override
  public void execute(W65C816 cpu)
  {
    if (width == M)
    {
      if (cpu.isMemory8Bit())
      {
        cpu.doneInstruction();
      }
    }
    else if (width == XY)
    {
      if (cpu.isIndex8Bit())
      {
        cpu.doneInstruction();
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
    return "DONE_If_8Bit_" + width;
  }

  public int getDone8()
  {
    return 1;
  }
}

