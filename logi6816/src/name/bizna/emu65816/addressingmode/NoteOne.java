package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.EmulatorException;
import name.bizna.emu65816.Width;
import name.bizna.emu65816.opcode.OpCode;

public class NoteOne
    extends Operation
{
  private Width width;

  public NoteOne(Width width)
  {
    this.width = width;
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
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
}

