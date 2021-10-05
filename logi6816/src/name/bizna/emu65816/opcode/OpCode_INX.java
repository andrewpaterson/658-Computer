package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_INX
    extends OpCode
{
  public OpCode_INX(int mCode, InstructionCycles cycles)
  {
    super("INX", "Increment Index X by One", mCode, cycles);
  }
}

