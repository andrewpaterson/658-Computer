package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_LSR_A
    extends OpCode
{
  public OpCode_LSR_A(int mCode, InstructionCycles cycles)
  {
    super("LSR", "Shift accumulator right one bit; update NZC.", mCode, cycles);
  }
}

