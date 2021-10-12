package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_TAX
    extends OpCode
{
  public OpCode_TAX(int mCode, InstructionCycles cycles)
  {
    super("TAX", "Transfer Accumulator in Index X", mCode, cycles);
  }
}

