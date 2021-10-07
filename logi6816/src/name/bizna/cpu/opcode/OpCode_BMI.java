package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_BMI
    extends OpCode
{
  public OpCode_BMI(int mCode, InstructionCycles cycles)
  {
    super("BMI", "Branch if Result Minus (N=1)", mCode, cycles);
  }
}

