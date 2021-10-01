package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PHD
    extends OpCode
{
  public OpCode_PHD(String mName, int mCode, InstructionCycles cycles)
  {
    super(mName, mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.push16Bit(cpu.getDirectPage());
    cpu.addToProgramAddressAndCycles(1, 4);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

