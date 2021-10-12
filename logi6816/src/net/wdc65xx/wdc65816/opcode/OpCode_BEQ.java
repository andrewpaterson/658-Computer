package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_BEQ
    extends OpCode
{
  public OpCode_BEQ(int mCode, InstructionCycles cycles)
  {
    super("BEQ", "Branch if Equal (Z=1)", mCode, cycles);
  }
}

