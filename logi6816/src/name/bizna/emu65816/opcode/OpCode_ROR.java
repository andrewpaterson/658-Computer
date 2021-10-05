package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_ROR
    extends OpCode
{
  public OpCode_ROR(int mCode, InstructionCycles cycles)
  {
    super("ROR", "Rotate memory right one bit; update NZC.", mCode, cycles);
  }
}

