package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class NoteOne
    extends Operation
{

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
  }

  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    return cpu.isMemory16Bit();
  }
}

