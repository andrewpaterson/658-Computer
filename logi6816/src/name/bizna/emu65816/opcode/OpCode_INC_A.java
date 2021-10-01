package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_INC_A
    extends OpCode
{
  public OpCode_INC_A(int mCode, InstructionCycles busCycles)
  {
    super("INC", "Increment accumulator; update NZ.", mCode, busCycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.incrementA();
  }
}

