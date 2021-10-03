package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PLP
    extends OpCode
{
  public OpCode_PLP(int mCode, InstructionCycles cycles)
  {
    super("PLP", "Pull Processor Status from Stack", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.getCpuStatus().setRegisterValue(cpu.pull8Bit());
    cpu.addToProgramAddressAndCycles(1, 4);
  }
}

