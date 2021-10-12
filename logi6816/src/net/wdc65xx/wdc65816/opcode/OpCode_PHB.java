package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PHB
    extends OpCode
{
  public OpCode_PHB(int mCode, InstructionCycles cycles)
  {
    super("PHB", "Push Data Bank Register on Stack", mCode, cycles);
  }
}

