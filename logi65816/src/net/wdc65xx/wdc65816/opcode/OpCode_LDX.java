package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_LDX
    extends OpCode
{
  public OpCode_LDX(int mCode, InstructionCycles cycles)
  {
    super("LDX", "Load Index X with Memory", mCode, cycles);
  }
}

