package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_ROR_A
    extends OpCode
{
  public OpCode_ROR_A(int mCode, InstructionCycles cycles)
  {
    super("ROR", "Rotate accumulator right one bit; update NZC.", mCode, cycles);
  }
}

