package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_JML
    extends OpCode
{
  public OpCode_JML(int mCode, InstructionCycles cycles)
  {
    super("JML", "Jump Long to New Location", mCode, cycles);
  }
}

