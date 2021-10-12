package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_BNE
    extends OpCode
{
  public OpCode_BNE(int mCode, InstructionCycles cycles)
  {
    super("BNE", "Branch if Not Equal (Z=0)", mCode, cycles);
  }
}

