package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_XBA
    extends OpCode
{
  public OpCode_XBA(int mCode, InstructionCycles cycles)
  {
    super("XBA", "Exchange B and A Accumulator", mCode, cycles);
  }
}

