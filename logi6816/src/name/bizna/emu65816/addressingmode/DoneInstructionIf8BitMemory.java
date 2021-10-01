package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class DoneInstructionIf8BitMemory
    extends Operation
{
  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    if (cpu.isMemory8Bit())
    {
      cpu.doneInstruction();
    }
  }
}

