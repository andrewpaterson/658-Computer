package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_INY
    extends OpCode
{
  public OpCode_INY(int mCode, InstructionCycles cycles)
  {
    super("INY", "Increment Index Y by One", mCode, cycles);
  }
}

