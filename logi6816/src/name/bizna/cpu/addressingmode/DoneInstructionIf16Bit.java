package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.util.EmulatorException;
import name.bizna.cpu.Width;

public class DoneInstructionIf16Bit
    extends Operation
{
  private final Width width;

  public DoneInstructionIf16Bit(Width width)
  {
    this.width = width;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    if (width == Width.A)
    {
      if (cpu.isMemory16Bit())
      {
        cpu.doneInstruction();
      }
    }
    else if (width == Width.XY)
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
}

