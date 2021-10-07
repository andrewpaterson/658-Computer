package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_COP
    extends OpCode
{
  public OpCode_COP(int code, InstructionCycles cycles)
  {
    super("COP", "Force co-processor software interrupt.", code, cycles);
  }
}

