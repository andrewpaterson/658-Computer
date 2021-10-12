package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_BIT
    extends OpCode
{
  public OpCode_BIT(int mCode, InstructionCycles cycles)
  {
    super("BIT", "Bit Test", mCode, cycles);
  }
}

