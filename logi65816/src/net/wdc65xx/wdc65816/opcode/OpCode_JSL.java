package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_JSL
    extends OpCode
{
  public OpCode_JSL(int mCode, InstructionCycles cycles)
  {
    super("JSL", "Jump long to new location save return address on Stack.", mCode, cycles);
  }
}

