package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_BRK
    extends OpCode
{
  public OpCode_BRK(int code, InstructionCycles instructionCycles)
  {
    super("BRK", "Force break software interrupt.", code, instructionCycles);
  }
}

