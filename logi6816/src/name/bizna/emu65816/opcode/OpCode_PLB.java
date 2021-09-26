package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PLB
    extends OpCode
{
  public OpCode_PLB(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.setDB(cpu.pull8Bit());
    cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(cpu.getDB());
    cpu.addToProgramAddressAndCycles(1, 4);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

