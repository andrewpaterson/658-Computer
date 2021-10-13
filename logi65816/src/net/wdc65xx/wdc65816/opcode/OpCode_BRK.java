package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_BRK
    extends OpCode
{
  public OpCode_BRK(int code, InstructionCycles cycles)
  {
    super("BRK",
          "Force break software interrupt.",
          code,
          cycles);
  }
}

