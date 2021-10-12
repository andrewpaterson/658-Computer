package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_STA
    extends OpCode
{
  public OpCode_STA(int mCode, InstructionCycles cycles)
  {
    super("STA", "Store Accumulator in Memory", mCode, cycles);
  }
}

