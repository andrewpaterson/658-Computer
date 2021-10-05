package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TRB
    extends OpCode
{
  public OpCode_TRB(int mCode, InstructionCycles cycles)
  {
    super("TRB", "Test and Reset Bit", mCode, cycles);
  }
}

