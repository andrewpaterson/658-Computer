package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

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

