package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_MVP
    extends OpCode
{
  public OpCode_MVP(int mCode, InstructionCycles cycles)
  {
    super("MVP", "Block move previous...", mCode, cycles);
  }
}

