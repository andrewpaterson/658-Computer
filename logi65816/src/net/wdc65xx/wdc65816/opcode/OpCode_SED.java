package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_SED
    extends OpCode
{
  public OpCode_SED(int mCode, InstructionCycles cycles)
  {
    super("SED", "Set Decimal Mode", mCode, cycles);
  }
}

