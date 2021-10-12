package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_STY
    extends OpCode
{
  public OpCode_STY(int mCode, InstructionCycles cycles)
  {
    super("STY", "Store Index Y in Memory", mCode, cycles);
  }
}

