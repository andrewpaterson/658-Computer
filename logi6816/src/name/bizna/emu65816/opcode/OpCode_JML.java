package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_JML
    extends OpCode
{
  public OpCode_JML(int mCode, InstructionCycles cycles)
  {
    super("JML", "Jump Long to New Location", mCode, cycles);
  }
}

