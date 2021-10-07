package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_LDA
    extends OpCode
{
  public OpCode_LDA(int mCode, InstructionCycles busCycles)
  {
    super("LDA", "Load Accumulator with Memory", mCode, busCycles);
  }
}

