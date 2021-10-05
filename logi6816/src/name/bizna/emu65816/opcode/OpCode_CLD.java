package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_CLD
    extends OpCode
{
  public OpCode_CLD(int mCode, InstructionCycles cycles)
  {
    super("CLD", "Clear Decimal Mode", mCode, cycles);
  }
}

