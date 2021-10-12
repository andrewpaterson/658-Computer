package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_CLC
    extends OpCode
{
  public OpCode_CLC(int mCode, InstructionCycles cycles)
  {
    super("CLC", "Clear Carry Flag", mCode, cycles);
  }
}

