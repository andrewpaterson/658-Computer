package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_STZ
    extends OpCode
{
  public OpCode_STZ(int mCode, InstructionCycles cycles)
  {
    super("STZ", "Store Zero in Memory", mCode, cycles);
  }
}

