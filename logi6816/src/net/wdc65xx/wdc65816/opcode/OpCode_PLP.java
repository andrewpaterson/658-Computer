package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PLP
    extends OpCode
{
  public OpCode_PLP(int mCode, InstructionCycles cycles)
  {
    super("PLP", "Pull Processor Status from Stack", mCode, cycles);
  }
}

