package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_BRA
    extends OpCode
{
  public OpCode_BRA(int mCode, InstructionCycles cycles)
  {
    super("BRA", "Branch Always", mCode, cycles);
  }
}

