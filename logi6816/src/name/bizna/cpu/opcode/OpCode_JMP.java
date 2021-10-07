package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_JMP
    extends OpCode
{
  public OpCode_JMP(int mCode, InstructionCycles cycles)
  {
    super("JMP", "Jump to New Location", mCode, cycles);
  }
}

