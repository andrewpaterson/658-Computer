package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_BPL
    extends OpCode
{
  public OpCode_BPL(int mCode, InstructionCycles cycles)
  {
    super("BPL", "Branch if Result Plus (N=0)", mCode, cycles);
  }
}

