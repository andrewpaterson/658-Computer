package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.EmulatorException;
import name.bizna.emu65816.Width;
import name.bizna.emu65816.opcode.OpCode;

public class DoneInstructionIf16Bit
    extends Operation
{
  private final Width width;

  public DoneInstructionIf16Bit(Width width)
  {
    this.width = width;
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
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
}

