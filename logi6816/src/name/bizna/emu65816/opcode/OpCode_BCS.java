package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_BCS
    extends OpCode
{
  public OpCode_BCS(int mCode, InstructionCycles cycles)
  {
    super("BCS", "Branch on Carry Set (C=1)", mCode, cycles);
  }
}

