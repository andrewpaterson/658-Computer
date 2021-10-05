package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_CPX
    extends OpCode
{
  public OpCode_CPX(int mCode, InstructionCycles cycles)
  {
    super("CPX", "Compare Memory and Index X", mCode, cycles);
  }
}

