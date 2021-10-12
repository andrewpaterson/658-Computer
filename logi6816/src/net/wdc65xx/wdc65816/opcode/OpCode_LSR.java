package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_LSR
    extends OpCode
{
  public OpCode_LSR(int mCode, InstructionCycles cycles)
  {
    super("LSR", "Shift memory right one bit; update NZC.", mCode, cycles);
  }
}

