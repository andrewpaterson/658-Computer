package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_NOP
    extends OpCode
{
  public OpCode_NOP(int mCode, InstructionCycles cycles)
  {
    super("NOP", "No Operation for two cycles.", mCode, cycles);
  }
}

