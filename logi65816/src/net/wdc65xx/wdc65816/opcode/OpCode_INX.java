package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_INX
    extends OpCode
{
  public OpCode_INX(int mCode, InstructionCycles cycles)
  {
    super("INX", "Increment Index X by One", mCode, cycles);
  }
}

