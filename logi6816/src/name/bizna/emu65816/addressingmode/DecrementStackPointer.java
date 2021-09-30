package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class DecrementStackPointer
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    cpu.decrementStackPointer();
  }
}

