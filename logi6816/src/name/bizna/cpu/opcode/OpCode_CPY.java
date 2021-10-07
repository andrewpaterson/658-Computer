package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_CPY
    extends OpCode
{
  public OpCode_CPY(int mCode, InstructionCycles cycles)
  {
    super("CPY", "Compare Memory and Index Y", mCode, cycles);
  }
}

