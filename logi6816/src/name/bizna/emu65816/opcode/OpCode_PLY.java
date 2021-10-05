package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PLY
    extends OpCode
{
  public OpCode_PLY(int mCode, InstructionCycles cycles)
  {
    super("PLY", "Pull Index Y from Stack", mCode, cycles);
  }
}

