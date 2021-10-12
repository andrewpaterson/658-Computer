package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_MVN
    extends OpCode
{
  public OpCode_MVN(int mCode, InstructionCycles cycles)
  {
    super("MVN", "Block move next...", mCode, cycles);
  }
}

