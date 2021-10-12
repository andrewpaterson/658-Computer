package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_NEXT
    extends OpCode
{
  public OpCode_NEXT(InstructionCycles cycles)
  {
    super("Fetch Opcode", "Fetch Opcode", -1, cycles);
  }
}

