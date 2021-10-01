package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_DEC_A
    extends OpCode
{
  public OpCode_DEC_A(int mCode, InstructionCycles busCycles)
  {
    super("DEC", "Decrement accumulator; update NZ.", mCode, busCycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    cpu.decrementA();
  }
}

