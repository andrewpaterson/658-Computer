package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

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

