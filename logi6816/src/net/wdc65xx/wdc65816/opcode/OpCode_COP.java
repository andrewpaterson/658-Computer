package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_COP
    extends OpCode
{
  public OpCode_COP(int code, InstructionCycles cycles)
  {
    super("COP", "Force co-processor software interrupt.", code, cycles);
  }
}

