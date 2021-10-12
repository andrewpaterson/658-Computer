package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_XCE
    extends OpCode
{
  public OpCode_XCE(int mCode, InstructionCycles cycles)
  {
    super("XCE", "Exchange Carry and Emulation Bits", mCode, cycles);
  }
}

