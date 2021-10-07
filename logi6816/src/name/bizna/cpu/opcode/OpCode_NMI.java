package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

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

