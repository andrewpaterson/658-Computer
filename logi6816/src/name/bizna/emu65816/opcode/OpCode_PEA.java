package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PEA
    extends OpCode
{
  public OpCode_PEA(int mCode, InstructionCycles cycles)
  {
    super("PEA", "Push Absolute Address", mCode, cycles);
  }
}

