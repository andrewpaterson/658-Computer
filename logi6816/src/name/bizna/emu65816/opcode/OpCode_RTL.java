package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_RTL
    extends OpCode
{
  public OpCode_RTL(int mCode, InstructionCycles cycles)
  {
    super("RTL", "Return from Subroutine Long", mCode, cycles);
  }
}

