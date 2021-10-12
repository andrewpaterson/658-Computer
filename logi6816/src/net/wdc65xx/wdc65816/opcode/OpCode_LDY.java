package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_LDY
    extends OpCode
{
  public OpCode_LDY(int mCode, InstructionCycles cycles)
  {
    super("LDY", "Load Index Y with Memory", mCode, cycles);
  }
}

