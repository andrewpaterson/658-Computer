package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PHX
    extends OpCode
{
  public OpCode_PHX(int mCode, InstructionCycles cycles)
  {
    super("PHX", "Push Index X on Stack", mCode, cycles);
  }
}

