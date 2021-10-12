package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_DEY
    extends OpCode
{
  public OpCode_DEY(int mCode, InstructionCycles cycles)
  {
    super("DEY", "Decrement Index Y by One", mCode, cycles);
  }
}

