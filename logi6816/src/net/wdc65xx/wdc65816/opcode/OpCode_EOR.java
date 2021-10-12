package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_EOR
    extends OpCode
{
  public OpCode_EOR(int mCode, InstructionCycles cycles)
  {
    super("EOR", "'Exclusive OR' Memory with Accumulator", mCode, cycles);
  }
}

