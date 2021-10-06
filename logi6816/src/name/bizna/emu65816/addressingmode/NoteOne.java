package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.EmulatorException;
import name.bizna.emu65816.Width;

public class NoteOne
    extends Operation
{
  private final Width width;

  public NoteOne(Width width)
  {
    this.width = width;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    if (width == Width.A)
    {
      return cpu.isMemory16Bit();
    }
    else if (width == Width.XY)
    {
      return cpu.isIndex16Bit();
    }
    else
    {
      throw new EmulatorException("Unknown Width.");
    }
  }

  @Override
  public String toString()
  {
    return "Note(1)";
  }
}

