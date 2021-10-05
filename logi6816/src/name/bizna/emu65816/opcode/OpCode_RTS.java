package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_RTS
    extends OpCode
{
  public OpCode_RTS(int mCode, InstructionCycles cycles)
  {
    super("RTS", "Return from Subroutine", mCode, cycles);
  }
}

