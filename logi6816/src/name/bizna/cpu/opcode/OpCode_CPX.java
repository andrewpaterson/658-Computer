package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_CPX
    extends OpCode
{
  public OpCode_CPX(int mCode, InstructionCycles cycles)
  {
    super("CPX", "Compare Memory and Index X", mCode, cycles);
  }
}

