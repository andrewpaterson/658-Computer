package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_BVS
    extends OpCode
{
  public OpCode_BVS(int mCode, InstructionCycles cycles)
  {
    super("BVS", "Branch on Overflow Set (V=1)", mCode, cycles);
  }
}

