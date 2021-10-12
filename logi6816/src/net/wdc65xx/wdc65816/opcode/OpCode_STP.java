package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_STP
    extends OpCode
{
  public OpCode_STP(int mCode, InstructionCycles cycles)
  {
    super("STP", "Stop the Clock", mCode, cycles);
  }
}

