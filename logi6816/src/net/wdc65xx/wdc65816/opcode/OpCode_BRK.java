package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.StackSoftwareInterruptCycles;

public class OpCode_BRK
    extends OpCode
{
  public OpCode_BRK(int code, StackSoftwareInterruptCycles cycles)
  {
    super("BRK",
          "Force break software interrupt.",
          code,
          cycles);
  }
}

