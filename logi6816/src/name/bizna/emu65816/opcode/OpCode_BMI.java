package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_BMI
    extends OpCode
{
  public OpCode_BMI(int mCode, InstructionCycles cycles)
  {
    super("BMI", "Branch if Result Minus (N=1)", mCode, cycles);
  }
}

