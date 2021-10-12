package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_RTI
    extends OpCode
{
  public OpCode_RTI(int mCode, InstructionCycles cycles)
  {
    super("RTI", "Return from Interrupt", mCode, cycles);
  }
}

