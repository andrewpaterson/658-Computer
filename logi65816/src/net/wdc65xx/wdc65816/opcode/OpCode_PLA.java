package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PLA
    extends OpCode
{
  public OpCode_PLA(int mCode, InstructionCycles cycles)
  {
    super("PLA", "Pull Accumulator from Stack", mCode, cycles);
  }
}

