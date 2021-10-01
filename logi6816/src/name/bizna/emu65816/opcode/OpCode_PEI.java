package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PEI
    extends OpCode
{
  public OpCode_PEI(String mName, int mCode, InstructionCycles cycles)
  {
    super("PEI", "Push Indirect Address", mCode, cycles);
  }
}

