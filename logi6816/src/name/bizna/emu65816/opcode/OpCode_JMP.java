package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_JMP
    extends OpCode
{
  public OpCode_JMP(int mCode, InstructionCycles cycles)
  {
    super("JMP", "Jump to New Location", mCode, cycles);
  }
}

