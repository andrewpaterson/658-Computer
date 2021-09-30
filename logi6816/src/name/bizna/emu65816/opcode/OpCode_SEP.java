package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_SEP
    extends OpCode
{
  public OpCode_SEP(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int value = cpu.getDataLow(cpu.getAddressOfOpCodeData(getAddressingMode()));
    if (cpu.getCpuStatus().isEmulationMode())
    {
      // In emulation mode status bits 4 and 5 are not affected
      // 0xCF = 11001111
      value &= 0xCF;
    }
    int statusReg = cpu.getCpuStatus().getRegisterValue();
    statusReg |= value;
    cpu.getCpuStatus().setRegisterValue(statusReg);

    cpu.addToProgramAddressAndCycles(2, 3);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

