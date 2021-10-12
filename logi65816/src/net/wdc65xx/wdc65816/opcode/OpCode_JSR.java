package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_JSR
    extends OpCode
{
  public OpCode_JSR(int mCode, InstructionCycles cycles)
  {
    super("JSR", "Jump to new location save return address on Stack.", mCode, cycles);
  }
}

