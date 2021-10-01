package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PER
    extends OpCode
{
  public OpCode_PER(String mName, int mCode, InstructionCycles cycles)
  {
    super("PER", "Push Program Counter Relative Address", mCode, cycles);
  }
}

