package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_RES
    extends OpCode
{
  public OpCode_RES(InstructionCycles cycles)
  {
    super("RES",
          "Reset the CPU.",
          -1,
          cycles);
  }
}

