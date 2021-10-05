package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_SEP
    extends OpCode
{
  public OpCode_SEP(int mCode, InstructionCycles cycles)
  {
    super("SEP", "Set Processor Status Bit", mCode, cycles);
  }
}

