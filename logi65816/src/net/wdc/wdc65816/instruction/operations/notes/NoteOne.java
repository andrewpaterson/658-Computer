package net.wdc.wdc65816.instruction.operations.notes;

import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.WidthFromRegister;
import net.util.EmulatorException;
import net.wdc.wdc65816.instruction.operations.Operation;

import static net.wdc.wdc65816.WidthFromRegister.M;
import static net.wdc.wdc65816.WidthFromRegister.XY;

public class NoteOne
    extends Operation
{
  private final WidthFromRegister width;

  public NoteOne(WidthFromRegister width)
  {
    this.width = width;
  }

  @Override
  public void execute(WDC65816 cpu)
  {
  }

  @Override
  public boolean mustExecute(WDC65816 cpu)
  {
    if (width == M)
    {
      return cpu.isMemory16Bit();
    }
    else if (width == XY)
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
