package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_WAI
    extends OpCode
{
  public OpCode_WAI(int mCode, InstructionCycles cycles)
  {
    super("WAI", "Wait for Interrupt", mCode, cycles);
  }
}

