package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

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

