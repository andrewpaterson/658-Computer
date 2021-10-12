package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.WidthFromRegister;
import name.bizna.util.EmulatorException;

import static name.bizna.cpu.WidthFromRegister.M;
import static name.bizna.cpu.WidthFromRegister.XY;

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
    return "DONE_8Bit_" + width;
  }

  @Override
  public boolean isDone()
  {
    return true;
  }
}

