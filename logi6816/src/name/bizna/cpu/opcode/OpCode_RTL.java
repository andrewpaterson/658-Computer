package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_RTL
    extends OpCode
{
  public OpCode_RTL(int mCode, InstructionCycles cycles)
  {
    super("RTL", "Return from Subroutine Long", mCode, cycles);
  }
}

