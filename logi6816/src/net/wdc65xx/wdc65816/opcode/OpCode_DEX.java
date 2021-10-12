package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_DEX
    extends OpCode
{
  public OpCode_DEX(int mCode, InstructionCycles cycles)
  {
    super("DEX", "Decrement Index X by One.", mCode, cycles);
  }
}

