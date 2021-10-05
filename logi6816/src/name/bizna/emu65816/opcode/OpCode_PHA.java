package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHA
    extends OpCode
{
  public OpCode_PHA(int mCode, InstructionCycles cycles)
  {
    super("PHA", "Push Accumulator onto Stack", mCode, cycles);
  }
}

