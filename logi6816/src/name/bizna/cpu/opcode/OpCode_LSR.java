package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_LSR
    extends OpCode
{
  public OpCode_LSR(int mCode, InstructionCycles cycles)
  {
    super("LSR", "Shift memory right one bit; update NZC.", mCode, cycles);
  }
}

