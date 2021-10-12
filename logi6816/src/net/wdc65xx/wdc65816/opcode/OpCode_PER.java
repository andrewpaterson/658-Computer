package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_PER
    extends OpCode
{
  public OpCode_PER(int mCode, InstructionCycles cycles)
  {
    super("PER", "Push Program Counter Relative Address", mCode, cycles);
  }
}

