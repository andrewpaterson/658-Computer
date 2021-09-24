package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toByte;

public class OpCode_REP
    extends OpCode
{
  public OpCode_REP(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    int value = cpu.readByte(cpu.getAddressOfOpCodeData(getAddressingMode()));
    int statusByte = cpu.getCpuStatus().getRegisterValue();
    cpu.getCpuStatus().setRegisterValue(toByte(statusByte & ~value));
    cpu.addToProgramAddressAndCycles(2, 3);
  }
}

