package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

import static name.bizna.emu65816.Unsigned.toByte;

public class NoteTwo
    extends DataOperation
{
  public NoteTwo()
  {
    super(false, false, true, true, true);
  }

  @Override
  public boolean mustExecute(Cpu65816 cpu)
  {
    return toByte(cpu.getDirectPage()) == 0;
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
  }
}

