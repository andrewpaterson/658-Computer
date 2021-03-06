package net.integratedcircuits.wdc.wdc65816.instruction.operations.notes;

import net.integratedcircuits.wdc.wdc65816.W65C816;
import net.integratedcircuits.wdc.wdc65816.WidthFromRegister;
import net.integratedcircuits.wdc.wdc65816.instruction.operations.Operation;
import net.util.EmulatorException;

import static net.integratedcircuits.wdc.wdc65816.WidthFromRegister.M;
import static net.integratedcircuits.wdc.wdc65816.WidthFromRegister.XY;

public class NoteOne
    extends Operation
{
  private final WidthFromRegister width;

  public NoteOne(WidthFromRegister width)
  {
    this.width = width;
  }

  @Override
  public void execute(W65C816 cpu)
  {
  }

  @Override
  public boolean mustExecute(W65C816 cpu)
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

