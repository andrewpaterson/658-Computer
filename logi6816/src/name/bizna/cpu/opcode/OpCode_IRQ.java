package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

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

