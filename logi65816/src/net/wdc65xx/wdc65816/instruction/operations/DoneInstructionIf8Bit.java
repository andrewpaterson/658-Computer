package net.wdc65xx.wdc65816.instruction.operations;

import net.util.EmulatorException;
import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.WidthFromRegister;

import static net.wdc65xx.wdc65816.WidthFromRegister.M;
import static net.wdc65xx.wdc65816.WidthFromRegister.XY;

public class DoneInstructionIf8Bit
    extends Operation
{
  private final WidthFromRegister width;

  public DoneInstructionIf8Bit(WidthFromRegister width)
  {
    this.width = width;
  }

  @Override
  public void execute(Cpu65816 cpu)
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
      throw new EmulatorException("Unknown Width.");
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
