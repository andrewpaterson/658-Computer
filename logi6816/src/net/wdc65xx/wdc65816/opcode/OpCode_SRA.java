package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_SRA
    extends OpCode
{
  public OpCode_SRA(int mCode, InstructionCycles cycles)
  {
    super("LSR", "Shift accumulator right one bit; update NZC.", mCode, cycles);
  }
}

