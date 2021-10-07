package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_CLD
    extends OpCode
{
  public OpCode_CLD(int mCode, InstructionCycles cycles)
  {
    super("CLD", "Clear Decimal Mode", mCode, cycles);
  }
}

