package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PHD
    extends OpCode
{
  public OpCode_PHD(int mCode, InstructionCycles cycles)
  {
    super("PHD", "Push Direct Register on Stack", mCode, cycles);
  }
}

