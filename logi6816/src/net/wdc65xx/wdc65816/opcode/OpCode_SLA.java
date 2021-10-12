package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_SLA
    extends OpCode
{
  public OpCode_SLA(int mCode, InstructionCycles busCycles)
  {
    super("ASL", "Shift accumulator left one bit; update NZC.", mCode, busCycles);
  }
}

