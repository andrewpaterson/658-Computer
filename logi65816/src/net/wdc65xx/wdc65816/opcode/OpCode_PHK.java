package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PHK
    extends OpCode
{
  public OpCode_PHK(int mCode, InstructionCycles cycles)
  {
    super("PHK", "Push Program Bank Register on Stack", mCode, cycles);
  }
}

