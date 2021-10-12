package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_WDM
    extends OpCode
{
  public OpCode_WDM(int mCode, InstructionCycles cycles)
  {
    super("WDM", "Reserved for future use", mCode, cycles);
  }
}

