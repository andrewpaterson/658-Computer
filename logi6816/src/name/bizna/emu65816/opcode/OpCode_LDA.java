package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_LDA
    extends OpCode
{
  public OpCode_LDA(int mCode, InstructionCycles busCycles)
  {
    super("LDA", "Load Accumulator with Memory", mCode, busCycles);
  }
}

