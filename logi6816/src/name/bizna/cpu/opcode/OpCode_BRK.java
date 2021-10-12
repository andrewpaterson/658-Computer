package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.StackSoftwareInterruptCycles;

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

