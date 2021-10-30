package net.wdc.wdc65816.instruction.operations;

import net.util.EmulatorException;
import net.wdc.wdc65816.W65C816;
import net.wdc.wdc65816.WidthFromRegister;

import static net.wdc.wdc65816.WidthFromRegister.M;
import static net.wdc.wdc65816.WidthFromRegister.XY;

public class DoneInstructionIf16Bit
    extends Operation
{
  private final WidthFromRegister width;

  public DoneInstructionIf16Bit(WidthFromRegister width)
  {
    this.width = width;
  }

  @Override
  public void execute(W65C816 cpu)
  {
    if (width == M)
    {
      if (cpu.isMemory16Bit())
      {
        cpu.doneInstruction();
      }
    }
    else if (width == XY)
    {
      if (cpu.isIndex16Bit())
      {
        cpu.doneInstruction();
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
    return "DONE_If_16Bit_" + width;
  }

  public int getDone16()
  {
    return 1;
  }
}

