package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_RLA
    extends OpCode
{
  public OpCode_RLA(int mCode, InstructionCycles cycles)
  {
    super("ROL", "Rotate Accumulator One Bit Left.", mCode, cycles);
  }
}

