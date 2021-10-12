package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_TDC
    extends OpCode
{
  public OpCode_TDC(int mCode, InstructionCycles cycles)
  {
    super("TDC", "Transfer Direct Register to C Accumulator", mCode, cycles);
  }
}

