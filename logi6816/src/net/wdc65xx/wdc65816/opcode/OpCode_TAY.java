package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_TAY
    extends OpCode
{
  public OpCode_TAY(int mCode, InstructionCycles cycles)
  {
    super("TAY", "Transfer Accumulator in Index Y", mCode, cycles);
  }
}

