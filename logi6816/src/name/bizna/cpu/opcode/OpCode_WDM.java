package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_WDM
    extends OpCode
{
  public OpCode_WDM(int mCode, InstructionCycles cycles)
  {
    super("WDM", "Reserved for future use", mCode, cycles);
  }
}

