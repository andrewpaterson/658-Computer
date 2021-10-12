package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PEI
    extends OpCode
{
  public OpCode_PEI(int mCode, InstructionCycles cycles)
  {
    super("PEI", "Push Indirect Address", mCode, cycles);
  }
}

