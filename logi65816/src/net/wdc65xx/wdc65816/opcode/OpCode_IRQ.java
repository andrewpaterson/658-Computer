package net.wdc65xx.wdc65816.opcode;

import net.wdc65xx.wdc65816.addressingmode.InstructionCycles;

public class OpCode_IRQ
    extends OpCode
{
  public OpCode_IRQ(InstructionCycles cycles)
  {
    super("IRQ",
          "Interrupt request.",
          -1,
          cycles);
  }
}

