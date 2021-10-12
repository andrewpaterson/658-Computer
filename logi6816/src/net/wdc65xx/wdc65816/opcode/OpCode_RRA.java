package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_RRA
    extends OpCode
{
  public OpCode_RRA(int mCode, InstructionCycles cycles)
  {
    super("ROR", "Rotate accumulator right one bit; update NZC.", mCode, cycles);
  }
}

