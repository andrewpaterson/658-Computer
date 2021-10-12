package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_RTL
    extends OpCode
{
  public OpCode_RTL(int mCode, InstructionCycles cycles)
  {
    super("RTL", "Return from Subroutine Long", mCode, cycles);
  }
}

