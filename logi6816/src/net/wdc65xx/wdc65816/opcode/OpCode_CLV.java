package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_CLV
    extends OpCode
{
  public OpCode_CLV(int mCode, InstructionCycles cycles)
  {
    super("CLV", "Clear Overflow Flag", mCode, cycles);
  }
}

