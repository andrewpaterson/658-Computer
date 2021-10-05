package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_SED
    extends OpCode
{
  public OpCode_SED(int mCode, InstructionCycles cycles)
  {
    super("SED", "Set Decimal Mode", mCode, cycles);
  }
}

