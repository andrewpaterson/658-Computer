package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_CPY
    extends OpCode
{
  public OpCode_CPY(int mCode, InstructionCycles cycles)
  {
    super("CPY", "Compare Memory and Index Y", mCode, cycles);
  }
}

