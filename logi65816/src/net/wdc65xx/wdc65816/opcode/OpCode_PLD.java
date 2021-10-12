package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PLD
    extends OpCode
{
  public OpCode_PLD(int mCode, InstructionCycles cycles)
  {
    super("PLD", "Pull Direct Register from Stack", mCode, cycles);
  }
}

