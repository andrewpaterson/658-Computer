package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PLB
    extends OpCode
{
  public OpCode_PLB(int mCode, InstructionCycles cycles)
  {
    super("PLB", "Pull Data Bank Register from Stack", mCode, cycles);
  }
}

