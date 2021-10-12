package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_CMP
    extends OpCode
{
  public OpCode_CMP(int mCode, InstructionCycles cycles)
  {
    super("CMP", "Compare Memory and Accumulator", mCode, cycles);
  }
}

