package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TSB
    extends OpCode
{
  public OpCode_TSB(int mCode, InstructionCycles cycles)
  {
    super("TSB", "Test and Set Bit", mCode, cycles);
  }
}

