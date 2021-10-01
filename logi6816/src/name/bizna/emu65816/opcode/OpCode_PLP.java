package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PLP
    extends OpCode
{
  public OpCode_PLP(String mName, int mCode, InstructionCycles cycles)
  {
    super(mName, mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.getCpuStatus().setRegisterValue(cpu.pull8Bit());
    cpu.addToProgramAddressAndCycles(1, 4);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

