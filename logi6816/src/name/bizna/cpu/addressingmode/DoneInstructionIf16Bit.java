package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.util.EmulatorException;
import name.bizna.cpu.WidthFromRegister;

import static name.bizna.cpu.WidthFromRegister.M;
import static name.bizna.cpu.WidthFromRegister.XY;

public class DoneInstructionIf16Bit
    extends Operation
{
  private final WidthFromRegister width;

  public DoneInstructionIf16Bit(WidthFromRegister width)
  {
    this.width = width;
  }

  @Override
  public void execute(Cpu65816 cpu)
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

  @Override
  public boolean isDone()
  {
    return true;
  }
}

