package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_REP
    extends OpCode
{
  public OpCode_REP(int mCode, InstructionCycles cycles)
  {
    super("REP", "Reset Status Bits", mCode, cycles);
  }
}

