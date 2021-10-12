package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PHA
    extends OpCode
{
  public OpCode_PHA(int mCode, InstructionCycles cycles)
  {
    super("PHA", "Push Accumulator onto Stack", mCode, cycles);
  }
}

