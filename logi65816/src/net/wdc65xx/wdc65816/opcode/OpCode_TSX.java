package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_TSX
    extends OpCode
{
  public OpCode_TSX(int mCode, InstructionCycles cycles)
  {
    super("TSX", "Transfer Stack Pointer Register to Index X", mCode, cycles);
  }
}

