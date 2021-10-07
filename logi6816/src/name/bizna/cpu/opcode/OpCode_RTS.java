package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_RTS
    extends OpCode
{
  public OpCode_RTS(int mCode, InstructionCycles cycles)
  {
    super("RTS", "Return from Subroutine", mCode, cycles);
  }
}

