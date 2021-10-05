package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_NMI
    extends OpCode
{
  public OpCode_NMI(InstructionCycles cycles)
  {
    super("NMI",
          "Non-maskable interrupt.",
          -1,
          cycles);
  }
}

