package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PLX
    extends OpCode
{
  public OpCode_PLX(int mCode, InstructionCycles cycles)
  {
    super("PLX", "Pull Index X from Stack", mCode, cycles);
  }
}

