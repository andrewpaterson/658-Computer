package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.util.EmulatorException;
import name.bizna.cpu.Width;

public class DoneInstructionIf8Bit
    extends Operation
{
  private final Width width;

  public DoneInstructionIf8Bit(Width width)
  {
    this.width = width;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (width == Width.A)
    {
      if (cpu.isMemory8Bit())
      {
        cpu.doneInstruction();
      }
    }
    else if (width == Width.XY)
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
}

