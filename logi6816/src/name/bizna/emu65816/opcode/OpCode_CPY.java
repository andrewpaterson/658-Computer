package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_CPY
    extends OpCode
{
  public OpCode_CPY(int mCode, InstructionCycles cycles)
  {
    super("CPY", "Compare Memory and Index Y", mCode, cycles);
  }
}

