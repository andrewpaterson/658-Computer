package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.util.EmulatorException;
import name.bizna.cpu.Width;

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

