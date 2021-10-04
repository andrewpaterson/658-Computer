package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_COP
    extends OpCode
{
  public OpCode_COP(int code, InstructionCycles cycles)
  {
    super("COP", "Force co-processor software interrupt.", code, cycles);
  }
}

