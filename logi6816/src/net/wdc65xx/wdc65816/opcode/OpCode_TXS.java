package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_TXS
    extends OpCode
{
  public OpCode_TXS(int mCode, InstructionCycles cycles)
  {
    super("TXS", " Transfer Index X to Stack Pointer Register", mCode, cycles);
  }
}

